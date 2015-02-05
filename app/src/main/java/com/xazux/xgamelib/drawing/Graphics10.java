package com.xazux.xgamelib.drawing;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.xazux.xgamelib.interfaces.ICircle;
import com.xazux.xgamelib.interfaces.IGameActivityContext;
import com.xazux.xgamelib.interfaces.IGraphics10;
import com.xazux.xgamelib.interfaces.ILine;
import com.xazux.xgamelib.interfaces.IPixelTexture;
import com.xazux.xgamelib.interfaces.IRect;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by josh on 05/02/15.
 */
public class Graphics10 implements IGraphics10 {
    HashMap<String, PixelTexture> _textureCache = new HashMap<>();

    @Override
    public void clear(int colour) {

    }

    @Override
    public void drawRect(IRect rect) {

    }

    @Override
    public void drawCircle(ICircle circle) {

    }

    @Override
    public void drawLine(ILine line, int colour) {

    }

    @Override
    public void drawPixel(int x, int y, int colour) {

    }

    @Override
    public void drawTexture(IPixelTexture texture, IRect rect) {

    }

    @Override
    public void drawTexture(IPixelTexture texture, IRect src, IRect dst) {

    }

    @Override
    public void drawTexture(IPixelTexture texture, IRect rect, int tintColour) {

    }

    @Override
    public void drawTexture(IPixelTexture texture, IRect src, IRect dst, int tintColour) {

    }

    @Override
    public IPixelTexture loadTexture(IGameActivityContext context, String asset, PixelTextureFormat format) {
        PixelTexture texture = null;
        if (_textureCache.containsKey(asset)) {
            texture = _textureCache.get(asset);
            if (!texture.hasBeenDisposed()) {
                return texture;
            }
        }

        Bitmap bitmap = null;
        try {
            InputStream inputStream = context.getAssets().open(asset);

            switch (format) {
                case ARGB4444:
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    break;
                case ARGB8888:
                    options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    break;
                default: //RGB565
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    break;
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (texture != null) {
            texture.reinstantiateBitmap(bitmap);
        }
        else {
            texture = new PixelTexture(bitmap);
        }
        return texture;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}
