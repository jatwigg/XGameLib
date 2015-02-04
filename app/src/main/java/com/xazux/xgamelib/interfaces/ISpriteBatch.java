package com.xazux.xgamelib.interfaces;

/**
 * Created by josh on 04/02/15.
 */
public interface ISpriteBatch extends IDrawingContext {
    void clear(int colour);
    void drawPixel(int x, int y, int colour);
    void drawLine(ILine line, int colour);
    void drawRect(IRect rect);
    void drawCircle(ICircle circle);
    void drawTexture(IPixelTexture texture, IRect rect);
    void drawTexture(IPixelTexture texture, IRect src, IRect dst);
    void drawTexture(IPixelTexture texture, IRect rect, int tintColour);
    void drawTexture(IPixelTexture texture, IRect src, IRect dst, int tintColour);
}