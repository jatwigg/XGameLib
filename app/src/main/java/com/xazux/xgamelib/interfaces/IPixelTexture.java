package com.xazux.xgamelib.interfaces;

import com.xazux.xgamelib.drawing.PixelTextureFormat;

/**
 * Created by josh on 04/02/15.
 */
public interface IPixelTexture {
    int getWidth();
    int getHeight();
    void dispose();
    android.graphics.Bitmap.Config getFormat();
}
