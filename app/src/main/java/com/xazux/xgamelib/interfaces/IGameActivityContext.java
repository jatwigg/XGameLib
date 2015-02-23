package com.xazux.xgamelib.interfaces;

import android.app.Activity;
import android.content.res.AssetManager;

import com.xazux.xgamelib.GameActivity;

/**
 * Created by josh on 04/02/15.
 */
public interface IGameActivityContext {
    void switchState(Class<IGameState> state);
    void switchState(Class<IGameState> state, Class<ITransitionState> transition);

    IJukeBox getJukebox();
    AssetManager getAssets();
    ISoundEffect getSoundEffects();

    void initialise();
    void update(float delta);
    void render(); //useful for debug information to be layered on top perhaps?
    void dispose();

    Activity getActivity();
}
