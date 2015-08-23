package com.example.pohguan.wallpaper;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PohGuan on 23/8/2015.
 */
public class MultiBitmapAnimationDrawer implements BitmapAnimationDrawer {

    /*
     * List of bitmaps.
     */
    private List<LiveWallpaperAnimatedBitmap> bitmaps;

    /*
     * Current index.
     */
    private int currentIndex;

    /*
     * Frame rate.
     */
    private int frameRate;

    /*
     * Time so far.
     */
    private int timeSoFar;

    /*
     * Cycle duration in ms.
     */
    private int cycleDuration;


    /*
     * Constructor.
     */
    public MultiBitmapAnimationDrawer(int frameRate) {
        bitmaps= new ArrayList<>();
        currentIndex = -1;
        this.frameRate = frameRate;
        timeSoFar = 0;
        cycleDuration = 0;
    }


    /*
     * Add frame.
     */
    public void addFrame(Bitmap bm, int left, int top, int duration) {
        LiveWallpaperAnimatedBitmap animBitmap = new LiveWallpaperAnimatedBitmap(bm, left, top, duration);
        bitmaps.add(animBitmap);
        cycleDuration += duration;
    }


    /*
     * Draw frame.
     */
    public void drawFrame(Canvas canvas) {
        int frameIndex = getDrawFrameIndex();

        if (frameIndex >= 0 && frameIndex < bitmaps.size()) {
            bitmaps.get(frameIndex).draw(canvas);
            currentIndex = frameIndex;
            timeSoFar += frameRate;
        }
    }


    /*
     * Get draw frame index.
     */
    public int getDrawFrameIndex() {
        if (timeSoFar == cycleDuration) {
            //cycle finishes
            timeSoFar = 0;
            return 0;
        } else if (timeSoFar < cycleDuration) {
            return nextFrameIndex();
        }

        return 0;
    }


    /*
     * Next Frame index.
     */
    private int nextFrameIndex() {
        int totalTime = 0;

        for (int i = 0; i < bitmaps.size(); i++) {
            totalTime += bitmaps.get(i).duration;
            if (totalTime > timeSoFar) {
                return i;
            }
        }

        return -1;
    }
}
