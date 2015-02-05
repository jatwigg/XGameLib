package com.xazux.xgamelib.sound;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.xazux.xgamelib.GameActivity;
import com.xazux.xgamelib.interfaces.IGameActivityContext;
import com.xazux.xgamelib.interfaces.IJukeBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by josh on 05/02/15.
 */
public class JukeBox implements IJukeBox {

    private ArrayList<String> _music = new ArrayList<>();
    private MediaPlayer _mediaPlayer = null;
    private Activity _context;
    private int _lastPlayedID = -1;

    public JukeBox(IGameActivityContext activity) {
        _context = activity.activity();
        _context.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public int loadMusic(String asset) {
        if (_music.contains(asset))
            return _music.indexOf(asset);
        _music.add(asset);
        return _music.size() - 1;
    }

    @Override
    public void playMusic(int musicID, boolean loop) {

        if (_mediaPlayer != null) {
            if( _lastPlayedID == musicID) {
                _mediaPlayer.start();
                return;
            }
            dispose();
        }

        String asset = _music.get(musicID);
        _mediaPlayer = new MediaPlayer();

        try {
            AssetFileDescriptor descriptor = _context.getAssets().openFd(asset);
            _mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            _mediaPlayer.prepare();
            _mediaPlayer.setLooping(loop);
            _mediaPlayer.start();
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Exception raised trying to play asset:" + asset + ", id:" + musicID, e);
            _mediaPlayer = null;
        }
    }

    @Override
    public void pauseMusic() {
        if (_mediaPlayer != null && _mediaPlayer.isPlaying())
            _mediaPlayer.pause();
    }

    @Override
    public void stopMusic() {
        if (_mediaPlayer != null && _mediaPlayer.isPlaying())
            _mediaPlayer.stop();
    }

    public void dispose() {
        if (_mediaPlayer != null) {
            stopMusic();
            _mediaPlayer.release();
            _mediaPlayer = null;
        }
    }
}
