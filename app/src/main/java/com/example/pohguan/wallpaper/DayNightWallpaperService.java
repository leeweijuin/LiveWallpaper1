package com.example.pohguan.wallpaper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.Random;


/**
 * Created by PohGuan on 4/7/2015.
 */
public class DayNightWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new DayNightWallpaperEngine();
    }

    private class DayNightWallpaperEngine extends Engine {
        /**
         * Handler.
         */
        private final Handler handler = new Handler();

        /**
         * Runnable.
         */
        private final Runnable drawRunner = new Runnable(){
            @Override
            public void run() {
                draw();
            }
        };

        /**
         * Visible? Why do I need this?
         */
        private boolean visible;

        /*
         * Callback interval (in milliseconds).
         */
        private int cbInterval;

        /*
         * Current color
         */
        private int currentRed, currentGreen, currentBlue;

        /*
         * Color increasing?
         */
        private boolean goingUp;

        /*
         * backgroundCenterX.
         */
        private float backgroundCenterX;

        /*
         * backgroundCenterY.
         */
        private float backgroundCenterY;

        /**
         * Constructor.
         */
        public DayNightWallpaperEngine( ){
            cbInterval = 10;
            currentRed = 60;
            currentGreen = 60;
            currentBlue = 60;
            goingUp = true;
            backgroundCenterX = 0f;
            backgroundCenterY = 0f;
            handler.post(drawRunner);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                SurfaceHolder holder = getSurfaceHolder();
                float x = event.getX();
                float y = event.getY();
            }

            super.onTouchEvent(event);
        }


        /**
         * Draw generic.
         */
        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    int[] colors = {0xFFFFFFFF, getCurrentColor()};
                    GradientDrawable grad = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
                    grad.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    grad.setGradientType(GradientDrawable.RADIAL_GRADIENT);
                    grad.setGradientRadius(canvas.getWidth());
                    grad.setGradientCenter(backgroundCenterX, backgroundCenterY);
                    grad.setColorFilter(0x77000022, PorterDuff.Mode.SRC_ATOP);
                    grad.draw(canvas);

                    ShapeDrawable shape1 = new ShapeDrawable(new OvalShape());
                    shape1.setBounds(0, 0, canvas.getWidth()/2, canvas.getHeight()/2);
                    shape1.getPaint().setColor(0x01FF0000);
                    shape1.draw(canvas);

                    updateCurrentColor();
                    updateCurrentBgCenter();
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }

            handler.removeCallbacks(drawRunner);
            handler.postDelayed(drawRunner, cbInterval);
        }


        /*
         * Current color.
         */
        private int getCurrentColor() {
            return Color.argb(60, currentRed, currentGreen, currentBlue);
        }


        /*
         * Update current color.
         */
        private void updateCurrentColor() {
            int maxColor = 120;
            int minColor = 60;

            if (goingUp) {
                if (currentGreen < maxColor) {
                    currentGreen++;
                } else if (currentRed < maxColor) {
                    currentRed++;
                } else if (currentBlue < maxColor) {
                    currentBlue++;
                } else {
                    goingUp = false;
                }
            }

            if (!goingUp) {
                if (currentRed > minColor) {
                    currentRed--;
                } else if (currentGreen > minColor) {
                    currentGreen--;
                } else if (currentBlue > minColor) {
                    currentBlue--;
                } else {
                    goingUp = true;
                }
            }
        }


        /*
         * Update background center.
         */
        private void updateCurrentBgCenter() {
            if (goingUp && backgroundCenterX < 1.0f) {
                backgroundCenterX += 0.01f;
            }

            if (!goingUp && backgroundCenterX > 0f) {
                backgroundCenterX -= 0.01f;
            }

        }
    }
}
