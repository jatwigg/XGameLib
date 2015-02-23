package com.xazux.xgamelib;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

import com.xazux.xgamelib.interfaces.IJukeBox;
import com.xazux.xgamelib.interfaces.ISoundEffect;
import com.xazux.xgamelib.interfaces.ITransitionState;
import com.xazux.xgamelib.sound.JukeBox;
import com.xazux.xgamelib.interfaces.IGameActivityContext;
import com.xazux.xgamelib.interfaces.IGameState;
import com.xazux.xgamelib.sound.SoundEffects;

/**
 * Created by josh on 05/02/15.
 */
public abstract class GameActivity extends Activity implements IGameActivityContext {
    private JukeBox _jukebox;
    private SoundEffects _soundEffects;
    private PowerManager.WakeLock _wakeLock;
    private GameLoopThread _thread;

    volatile boolean isInitialised = false;
    volatile IGameState currentState;
    volatile Class<IGameState> desiredState;
    volatile Class<ITransitionState> transitionState;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setupGraphics();

        PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        _wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, getCallingPackage() + "wakelock"); //TODO investigate the use of FLAG_KEEP_SCREEN_ON (API 17 and above)
    }

    protected abstract GLSettings setupGraphics();

    @Override
    protected void onResume() {
        super.onResume();
        if (!_wakeLock.isHeld())
            _wakeLock.acquire();
        // check existing thread is finished
        _thread.startThreads();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (_wakeLock.isHeld())
            _wakeLock.release();
        _thread.endThreads();
        if (isFinishing()) {
            // activity is closing, dispose stuff
            internalDispose();
        }
    }

    @Override
    public void switchState(Class<IGameState> state) {
        if (state == null) {
            finish();
        } else {
            transitionState = null;
            desiredState = state;
        }
    }

    @Override
    public void switchState(Class<IGameState> state, Class<ITransitionState> transition) {
        if (state == null) {
            finish();
        } else {
            transitionState = transition;
            desiredState = state;
        }
    }

    @Override
    public IJukeBox getJukebox() {
        return _jukebox;
    }

    @Override
    public ISoundEffect getSoundEffects() {
        return _soundEffects;
    }

    void internalInitialise() {
        _jukebox = new JukeBox(this);
        _soundEffects = new SoundEffects(this);
        initialise();
    }

    void internalDispose() {
        _jukebox.dispose();
        _soundEffects.dispose();
        dispose();
    }
}
