package com.xazux.xgamelib;

import android.os.SystemClock;
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
    private final GameActivity _gameActivity;
    private volatile boolean _isRunning = true; // volatile, so 'setting' thread and 'this' thread don't have an inconsistent value for it
    private volatile IGameState newReadyState = null;
    private volatile boolean _threadHasBeenToldToEnd = false;

    public GameLoopThread(GameActivity context) {
        _gameActivity = context;
        _thread = new Thread(this);
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

        while (_isRunning) {
            // get time now
            startTime = System.nanoTime();
            timeDiff = startTime - endTime;

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
                    _gameActivity.currentState.initialise(_gameActivity);
                }
                // now start initialising desired game state in another thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // create instance
                        IGameState desiredGameState;
                        try {
                            desiredGameState = instantiate(_gameActivity.desiredState);
                        }
                        catch (Exception e) {
                            Log.e(getClass().getSimpleName(), "Exception raised trying to instantiate new desired state", e);
                            throw new RuntimeException(e);
                        }
                        // init instance
                        desiredGameState.initialise(_gameActivity);
                        // let main thread know states ready for updating
                        newReadyState = desiredGameState;
                        // if thread has exited, go ahead and dispose and update current thread
                        if (_threadHasBeenToldToEnd) {
                            if (_gameActivity.currentState != null) {
                                _gameActivity.currentState.dispose();
                            }
                            //else race condition - todo solve this
                            _gameActivity.currentState = newReadyState;
                        }
                    }
                }).start();
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
        Log.d(this.getClass().getSimpleName(), "/GameLoopThread");
    }

    private IGameState instantiate(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> ctor = clazz.getConstructor(IGameActivityContext.class); //this class is the constructor param
        return (IGameState) ctor.newInstance(new Object[]{this});
    }

    public void startThread() {
        while (_thread != null && _thread.isAlive()) {
            try {
                _thread.interrupt();
                _thread.join();
            }
            catch (Exception e) {
                Log.d(getClass().getSimpleName(), "Thread exception ", e);
            }
        }
        // start new thread
        _thread = new Thread(this);
        _thread.start();
    }
}
