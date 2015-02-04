package com.xazux.xgamelib.interfaces;

/**
 * Created by josh on 04/02/15.
 */
public interface ICollideableShape {
    boolean collidesWith(IRect rect);
    boolean collidesWith(ICircle circle);
    boolean collidesWith(ILine line);
    boolean collidesWith(IPolygon line);

    void setRotation(float r);
    float getRotation();

    void offsetTo(float x, float y);
    void offsetBy(float x, float y);
    void offsetSoCenterIs(float x, float y);

    float getCenterX();
    float getCenterY();
}
