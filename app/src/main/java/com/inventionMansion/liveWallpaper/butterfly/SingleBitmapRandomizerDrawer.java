package com.inventionMansion.liveWallpaper.butterfly;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;

import java.util.List;
import java.util.Random;

/**
 * Created by PohGuan on 9/1/2015.
 */
public class SingleBitmapRandomizerDrawer implements BitmapAnimationDrawer {

    /*
     * The bitmap.
     */
    private Bitmap bm;

    /*
     * Left.
     */
    public int left;

    /*
     * Top.
     */
    public int top;

    /*
     * Rotation.
     */
    private float angle;

    /*
     * xScale.
     */
    private float xScale;

    /*
     * yScale.
     */
    private float yScale;

    /*
     * Time Manager.
     */
    private TimeManager timeManager;


    /*
     * Constructor.
     */
    public SingleBitmapRandomizerDrawer(Bitmap bm, TimeManager timeManager) {
        this.bm = bm;
        this.timeManager = timeManager;
    }


    /*
     * Draw frame.
     */
    public void drawFrame(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.postTranslate(-bm.getWidth()/2, -bm.getHeight()/2);
        matrix.postRotate(angle);
        matrix.postTranslate(bm.getWidth() / 2, bm.getHeight() / 2);
        matrix.postScale(xScale, yScale);
        matrix.postTranslate(left, top);

        canvas.drawBitmap(bm, matrix, null);
    }


    /*
     * Randomize stuff.
     */
    public void randomize(Canvas canvas, List<Point> myList) {
        Random r = new Random();
        angle = 60 - (r.nextFloat() * 120);

        setScale();
        int offScreenOffsetX = (int) (bm.getWidth()*xScale);
        int offScreenOffsetY = (int) (bm.getHeight()*yScale);

        int noOfSample = 100;
        double bestDistance = 0;
        Point bestPoint = null;

        Random random = new Random();
        if (myList != null) {
            if (myList.isEmpty()) {
                left = random.nextInt(canvas.getWidth() - offScreenOffsetX); // - offScreenOffsetX/2;// canvas.getWidth() - 210;//offScreenOffsetX;
                top = random.nextInt(canvas.getHeight() - offScreenOffsetY); // - offScreenOffsetY/2;//canvas.getHeight() - 250;
                bestPoint = new Point(left, top);
                myList.add(bestPoint);
            } else {
                for (int i = 0; i < noOfSample; i++) {
                    int x = (int) (random.nextInt(canvas.getWidth() - offScreenOffsetX));
                    int y = (int) (random.nextInt(canvas.getHeight() - offScreenOffsetY));
                    Point p = new Point(x, y);
                    Point closestP = findClosest(myList, p);
                    double d = getDistance(closestP, p);

                    if (d > bestDistance) {
                        bestDistance = d;
                        bestPoint = p;
                    }
                }
                if (bestPoint != null) {
                    left = bestPoint.x;
                    top = bestPoint.y;
                    myList.add(bestPoint);
                }

            }
        }
    }


    /*
     * Find the closest point.
     */
    public Point findClosest(List<Point> theList, Point theNewP) {
        Point closestPoint = theNewP;
        double shortestDistance = -1;
        for (int i = 0; i < theList.size(); i++) {
            Point p = theList.get(i);
            double d = getDistance(p, theNewP);
            if (shortestDistance < 0 || shortestDistance > d) {
                closestPoint = p;
                shortestDistance = d;
            }
        }
        return closestPoint;
    }


   /*
    * Get Distance.
    */
    public double getDistance(Point p1, Point p2) {
        double n1 = Math.pow((p2.y - p1.y),2);
        double n2 = Math.pow((p2.x - p1.x), 2);
//        System.out.println("n1 " + n1 + " n2 " + n2);
        double d = Math.sqrt(n1 + n2);
        return d;
    }


    /*
     * Set scale of bitmap.
     */
    private void setScale() {
        Random r = new Random();
        float randFloat = r.nextFloat();

        xScale = 0.7f;
        yScale = 0.7f;

        xScale -= randFloat*0.3f*xScale;
        yScale -= randFloat*0.3f*yScale;
    }
}
