package com.xazux.xgamelib.interfaces;

/**
 * Created by josh on 05/02/15.
 */
public interface ISoundEffect {
    int loadSound(String asset);
    void playSound(int soundID);
    void playSoundLooped(int soundID);
    void stopSound(int soundID);
}