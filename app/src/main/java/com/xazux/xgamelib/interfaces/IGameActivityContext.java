package com.xazux.xgamelib.interfaces;

import android.app.Activity;
import android.content.res.AssetManager;

import com.xazux.xgamelib.GameActivity;

/**
 * Created by josh on 04/02/15.
 */
public interface IGameActivityContext extends IDrawingContext {
    void registerGameState(Class<IGameState> state);
    void registerGameState(Class<IGameState> state, Class<IGameState> transitionState);

    AssetManager getAssets();
    IJukeBox jukebox();
    ISoundEffect soundEffects();

    void initialise();
    void update(float delta);
    void render(); //useful for debug information to be layered on top perhaps?
    void dispose();

    Activity activity();
}
