package com.xazux.xgamelib.interfaces;

/**
 * Created by josh on 04/02/15.
 */
public interface IRect extends ICollideableShape {
    float left();
    float top();
    float right();
    float bottom();

    float width();
    float height();

    void left(float f);
    void top(float f);
    void right(float f);
    void bottom(float f);

}
