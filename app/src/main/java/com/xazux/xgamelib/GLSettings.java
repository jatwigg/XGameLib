package com.xazux.xgamelib;

import android.app.Activity;
import android.app.Service;
import android.view.WindowManager;

import com.xazux.xgamelib.interfaces.IGameActivityContext;

/**
 * Created by josh on 22/02/15.
 */
class GLSettings {
    public final int width;
    public final int height;
    public final boolean isLandscape;

    public GLSettings(final int designedWidth, final int designedHeight, final boolean isLandscape) {
        this.width = designedWidth;
        this.height = designedHeight;
        this.isLandscape = isLandscape;
    }
}
