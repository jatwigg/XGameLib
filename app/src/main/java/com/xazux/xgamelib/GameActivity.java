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

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        _wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, getCallingPackage() + "wakelock"); //TODO investigate the use of FLAG_KEEP_SCREEN_ON (API 17 and above)
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!_wakeLock.isHeld())
            _wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (_wakeLock.isHeld())
            _wakeLock.release();
        if (isFinishing()) {
            // activity is closing, dispose stuff
            _jukebox.dispose();
        }
    }

    @Override
    public void registerGameState(Class<IGameState> state) {

    }

    @Override
    public void registerGameState(Class<IGameState> state, Class<IGameState> transitionState) {

    }

    @Override
    public IJukeBox jukebox() {
        return _jukebox;
    }

    @Override
    public ISoundEffect soundEffects() {
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

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}
