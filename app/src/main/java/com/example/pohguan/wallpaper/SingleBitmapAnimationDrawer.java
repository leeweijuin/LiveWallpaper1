package com.example.pohguan.wallpaper;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by PohGuan on 23/8/2015.
 */
public class SingleBitmapAnimationDrawer implements BitmapAnimationDrawer {

    /*
     * The bitmap.
     */
    private Bitmap bm;

    /*
     * Left.
     */
    private int left;

    /*
     * Top.
     */
    private int top;

    /*
     * Left change.
     */
    private int leftChange;

    /*
     * Top Change.
     */
    private int topChange;

    /*
     * Left.
     */
    private int initLeft;

    /*
     * Top.
     */
    private int initTop;


    public SingleBitmapAnimationDrawer(Bitmap bm, int left, int top, int leftChange, int topChange) {
        this.bm = bm;
        this.left = left;
        this.top = top;
        this.leftChange = leftChange;
        this.topChange = topChange;
        this.initLeft = left;
        this.initTop = top;
    }


    /*
     * Draw frame.
     */
    public void drawFrame(Canvas canvas) {
        canvas.drawBitmap(bm, left, top, null);
        updatePos(canvas);
    }


    /*
     * Update Pos.
     */
    public void updatePos(Canvas canvas) {
        if (left > canvas.getWidth()) {
            left = initLeft;
            top = initTop;
        } else if (top > canvas.getHeight()) {
            left = initLeft;
            top = initTop;
        } else {
            left += leftChange;
            top += topChange;
        }
    }

}
