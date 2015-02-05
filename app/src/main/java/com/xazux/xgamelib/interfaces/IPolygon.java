package com.xazux.xgamelib.interfaces;

/**
 * Created by josh on 04/02/15.
 */
public interface IPolygon extends ICollideableShape {
    void putPoint(IVector2D point);
    void putPoint(float x, float y);

    int pointCount();
    IVector2D point(int position);

    int lineCount();
    ILineSegment line(int position);
    ILineSegment[] lines();

    boolean isValid(); // has >= 3 points (and perhaps lines to not cross?)
}
