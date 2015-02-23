package com.xazux.xgamelib;

import android.util.Log;

import com.xazux.xgamelib.interfaces.IGameActivityContext;
import com.xazux.xgamelib.interfaces.IGameState;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by josh on 22/02/15.
 */
class GameLoopThread implements Runnable {

    // timings
    private int _maxFPS = 30;
    private int _maxFrameSkips = 5;
    private long _framePeriodMills = 1000 / _maxFPS;

    // context and state
    private volatile Thread _thread;
    private volatile Thread _threadStateSwitcher;

    private final GameActivity _gameActivity;
    private volatile boolean _stateSwitchingThreadShouldRun = true;
    private volatile boolean _gameLoopShouldRun = true;
    private volatile boolean _newStateIsReady = false;
    private volatile IGameState newReadyState = null;

    public GameLoopThread(GameActivity context) {
        _gameActivity = context;
    }

    @Override
    public void run() {
        Log.d(this.getClass().getSimpleName(), "GameLoopThread start");

        if (!_gameActivity.isInitialised) {
            this._gameActivity.internalInitialise();
            this._gameActivity.initialise();
            _gameActivity.isInitialised = true;
        }

        if (_gameActivity.currentState == null) {
            if (_gameActivity.desiredState == null) {
                throw new NoStartStateException();
            }
        }

        long startTime, timeDiff, endTime = System.nanoTime(), sleepTime;
        int framesSkipped;

        while (_gameLoopShouldRun) {
            // get time now
            startTime = System.nanoTime();
            timeDiff = startTime - endTime;

            if (_newStateIsReady) {
                // set new state
                _gameActivity.currentState = newReadyState;
                if (_threadStateSwitcher != null) {
                    _threadStateSwitcher.interrupt();
                }
                _newStateIsReady = false;
                // set new state
                _gameActivity.currentState = newReadyState;
            }
            // check for state change
            if (_gameActivity.desiredState != null) {
                // finish current state (null if first time
                if (_gameActivity.currentState != null) {
                    _gameActivity.currentState.dispose();
                }
                // init new transition state (if there is one)
                if (_gameActivity.transitionState != null) {
                    try {
                        _gameActivity.currentState = instantiate(_gameActivity.transitionState);
                    }
                    catch (Exception e) {
                        Log.e(getClass().getSimpleName(), "Exception raised trying to instantiate new transition state", e);
                        throw new RuntimeException(e);
                    }
                    //clear state
                    _gameActivity.transitionState = null;
                    // initialise transition
                    _gameActivity.currentState.initialise(_gameActivity);
                }
                // now start initialising desired game state in another thread
                _threadStateSwitcher.interrupt();
            }

            // update game logic
            _gameActivity.update(timeDiff);
            _gameActivity.currentState.update(timeDiff);

            synchronized (this._gameActivity) {
                // draw stuff
                _gameActivity.currentState.render();
                _gameActivity.render();
            }
        }
        // game loop ended


        Log.d(this.getClass().getSimpleName(), "/GameLoopThread");
    }

    private IGameState instantiate(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> ctor = clazz.getConstructor(IGameActivityContext.class); //this class is the constructor param
        return (IGameState) ctor.newInstance(new Object[]{this});
    }

    public void endThreads() {
        _gameLoopShouldRun = false;
        _stateSwitchingThreadShouldRun = false;

        while (_thread != null && _thread.isAlive()) {
            try {
                _thread.interrupt();
                _thread.join();
            }
            catch (Exception e) {
                Log.d(getClass().getSimpleName(), "Thread exception ", e);
            }
        }

        while (_threadStateSwitcher != null && _threadStateSwitcher.isAlive()) {
            try {
                _threadStateSwitcher.interrupt();
                _threadStateSwitcher.join(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startThreads() {
        endThreads();
        // start new threads
        _threadStateSwitcher = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (_stateSwitchingThreadShouldRun) {
                    // create instance
                    IGameState desiredGameState;
                    try {
                        desiredGameState = instantiate(_gameActivity.desiredState);
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), "Exception raised trying to instantiate new desired state", e);
                        throw new RuntimeException(e);
                    }
                    // clear state
                    _gameActivity.desiredState = null;

                    // init instance
                    desiredGameState.initialise(_gameActivity);
                    // let main thread know states ready for updating
                    newReadyState = desiredGameState;
                    _newStateIsReady = true;

                    // save transition state
                    IGameState transitionState = _gameActivity.currentState;

                    // wait to be woken up by main thread, then we know its safe to dispose and switch states
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        //ready
                    }

                    // dispose and transition state
                    if (transitionState != null) {
                        transitionState.dispose();
                    }
                    // i am complete
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        });
        _threadStateSwitcher.start();
        _thread = new Thread(this);
    }
}
