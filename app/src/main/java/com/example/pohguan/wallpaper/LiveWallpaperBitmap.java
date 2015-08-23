package com.example.pohguan.wallpaper;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by PohGuan on 23/8/2015.
 */
public abstract class LiveWallpaperBitmap {

    /*
     * The Bitmap.
     */
    protected Bitmap bm;

    /*
     * From top.
     */
    protected int top;

    /*
     * From left.
     */
    protected int left;


    /*
     * Constructor.
     */
    public LiveWallpaperBitmap(Bitmap bm, int left, int top) {
        this.bm = bm;
        this.left = left;
        this.top = top;
    }


    /*
     * Draw.
     */
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bm, left, top, null);
    }
}
