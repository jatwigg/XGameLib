package com.xazux.xgamelib.interfaces;

/**
 * Created by josh on 04/02/15.
 */
public interface ILine extends ICollideableShape {
    void point1(IVector2D point1);
    void point2(IVector2D point2);

    IVector2D point1();
    IVector2D point2();
}
