package com.xazux.xgamelib.interfaces;

/**
 * Created by josh on 04/02/15.
 */
public interface IGameState {
    void initialise();
    void update(float deltaTime);
    void render(ISpriteBatch spriteBatch);
    void dispose();

    void pause();
    void resume();
    boolean backPressed(); //return true if you handle this
}
