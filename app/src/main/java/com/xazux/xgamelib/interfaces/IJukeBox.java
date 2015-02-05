package com.xazux.xgamelib.interfaces;

/**
 * Created by josh on 05/02/15.
 */
public interface IJukeBox {
    int loadMusic(String asset);
    void playMusic(int musicID, boolean loop);
    void pauseMusic();
    void stopMusic();
    //TODO: ensure there is a release() method  that gets called in onPause if (isFIniishing());
}
