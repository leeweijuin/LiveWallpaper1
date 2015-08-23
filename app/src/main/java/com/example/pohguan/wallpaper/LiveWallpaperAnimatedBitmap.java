package com.example.pohguan.wallpaper;

import android.graphics.Bitmap;

/**
 * Created by PohGuan on 23/8/2015.
 */
public class LiveWallpaperAnimatedBitmap extends LiveWallpaperBitmap {

    /*
     * Duration.
     */
    public int duration;

    /*
     * Constructor.
     */
    public LiveWallpaperAnimatedBitmap(Bitmap bm, int left, int top, int duration) {
        super(bm, left, top);
        this.duration = duration;
    }
}
