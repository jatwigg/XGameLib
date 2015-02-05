package com.xazux.xgamelib.drawing;

import android.graphics.Bitmap;
import android.util.Log;

import com.xazux.xgamelib.interfaces.IPixelTexture;

/**
 * Created by josh on 05/02/15.
 */
public class PixelTexture implements IPixelTexture {
    private Bitmap _bitmap;

    public PixelTexture(Bitmap bitmap) {
        _bitmap = bitmap;
    }

    @Override
    public int getWidth() {
        return _bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return _bitmap.getHeight();
    }

    @Override
    public void dispose() {
        _bitmap.recycle();
    }

    @Override
    public Bitmap.Config getFormat() {
        return _bitmap.getConfig();
    }

    public boolean hasBeenDisposed() {
        return _bitmap.isRecycled();
    }

    public void reinstantiateBitmap(Bitmap bitmap) {
        if (!_bitmap.isRecycled()) {
            Log.e(getClass().getSimpleName(), "reinstantiateBitmap() was called FOR texture with a active bitmap.");
            _bitmap.recycle();
        }
        _bitmap = bitmap;
    }
}
