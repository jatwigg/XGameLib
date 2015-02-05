package com.xazux.xgamelib.sound;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import com.xazux.xgamelib.interfaces.IGameActivityContext;
import com.xazux.xgamelib.interfaces.ISoundEffect;

import java.io.IOException;

/**
 * Created by josh on 05/02/15.
 */
public class SoundEffects implements ISoundEffect {

    private final Activity _context;
    private SoundPool _soundPool;

    public SoundEffects(IGameActivityContext context) {
        _context = context.activity();
        _context.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            _soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        }
        else {
            SoundPool.Builder b = new SoundPool.Builder();
            b.setMaxStreams(4);
            b.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build());
            _soundPool = b.build();
        }
    }

    @Override
    public int loadSound(String asset) {
        try {
            AssetFileDescriptor descriptor = _context.getAssets().openFd("explosion.ogg");
            return _soundPool.load(descriptor, 1);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Exception raised trying to load sound effect asset:" + asset, e);
        }
        return -1;
    }

    @Override
    public void playSound(int soundID) {
        if (soundID > -1)
            _soundPool.play(soundID, 1, 1, 0, 0, 1);
    }

    @Override
    public void playSoundLooped(int soundID) {
        if (soundID > -1)
            _soundPool.play(soundID, 1, 1, 0, 1, 1); //investigate if loop works
    }

    @Override
    public void stopSound(int soundID) {
        if (soundID > -1)
            _soundPool.stop(soundID);
    }

    public void dispose() {
        _soundPool.release();
    }
}
