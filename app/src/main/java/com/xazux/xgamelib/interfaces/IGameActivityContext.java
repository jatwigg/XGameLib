package com.xazux.xgamelib.interfaces;

/**
 * Created by josh on 04/02/15.
 */
public interface IGameActivityContext extends IDrawingContext {
    void registerGameState(Class<IGameState> state);
    void registerGameState(Class<IGameState> state, Class<IGameState> transitionState);
    void initialise();
    void dispose();
}
