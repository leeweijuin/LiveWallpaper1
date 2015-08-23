package com.example.pohguan.wallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;


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
         * Frame rate (in milliseconds).
         */
        private int frameRate;

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

        /*
         * Animation drawers
         */
        private List<BitmapAnimationDrawer> animations;

        /**
         * Constructor.
         */
        public DayNightWallpaperEngine( ){
            frameRate = 30;
            currentRed = 60;
            currentGreen = 60;
            currentBlue = 60;
            goingUp = true;
            backgroundCenterX = 0f;
            backgroundCenterY = 0f;
            setupAnimations();
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
                    canvas.drawColor(getResources().getColor(android.R.color.black));

                    drawBgGradient(canvas);
                    drawMoon(canvas);
                    drawMountain(canvas, canvas.getWidth() / 2, 2 * canvas.getHeight() / 3, canvas.getWidth() / 2, canvas.getHeight() / 6);
                    drawMountain(canvas, 0, 2 * canvas.getHeight() / 3, 2 * canvas.getWidth() / 3, canvas.getHeight() / 5);
                    drawBitmaps(canvas);

                    updateCurrentColor();
                    updateCurrentBgCenter();
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }

            handler.removeCallbacks(drawRunner);
            handler.postDelayed(drawRunner, frameRate);
        }


        /*
         * Draw bitmaps.
         */
        private void drawBitmaps(Canvas canvas) {
            for (int i=0; i<animations.size(); i++) {
                animations.get(i).drawFrame(canvas);
            }
        }


        /*
         * Draw Background Gradient.
         */
        private void drawBgGradient(Canvas canvas) {
            int[] colors = {getResources().getColor(android.R.color.white), getCurrentColor()};
            GradientDrawable grad = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            grad.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            grad.setGradientType(GradientDrawable.RADIAL_GRADIENT);
            grad.setGradientRadius(canvas.getWidth());
            grad.setGradientCenter(backgroundCenterX, backgroundCenterY);
            grad.setColorFilter(0x77000022, PorterDuff.Mode.SRC_ATOP);
            grad.draw(canvas);
        }


        /*
         * Draw moon
         */
        private void drawMoon(Canvas canvas) {
            ShapeDrawable moon = new ShapeDrawable(new OvalShape());
            int radius = canvas.getWidth()/4;
            moon.setBounds(radius, radius, 3 * radius, 3 * radius);
            moon.getPaint().setColor(0xFF888822);
            moon.draw(canvas);
        }


        /*
         * Draw mountains
         */
        private void drawMountain(Canvas canvas, int x, int y, int width, int height) {
            Path mountain = new Path();
            mountain.moveTo(x, y);
            mountain.lineTo(x + width/2, y-height);
            mountain.lineTo(x + width, y);
            mountain.close();

            /*Paint paint = new Paint();
            paint.setColor(Color.DKGRAY);
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.STROKE);
*/
            int darkGreen = getResources().getColor(R.color.dark_green);
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
            p.setColor(0xff800000);
            p.setShader(new LinearGradient(x+width/2, y, x+width/2, y-height, Color.BLACK, darkGreen, Shader.TileMode.CLAMP));
            canvas.drawPath(mountain, p);
        }


        /*
         * Setup animation.
         */
        private void setupAnimations() {
            BitmapAnimationDrawer cloudDrawer = new BitmapAnimationDrawer(frameRate);
            Bitmap cloud = BitmapFactory.decodeResource(getResources(), R.drawable.cloud);
            cloudDrawer.addFrame(cloud, 20, 180, frameRate);
            cloudDrawer.addFrame(cloud, 30, 180, frameRate);
            cloudDrawer.addFrame(cloud, 40, 180, frameRate);
            cloudDrawer.addFrame(cloud, 50, 180, frameRate);
            cloudDrawer.addFrame(cloud, 60, 180, frameRate);
            cloudDrawer.addFrame(cloud, 70, 180, frameRate);
            cloudDrawer.addFrame(cloud, 80, 180, frameRate);
            cloudDrawer.addFrame(cloud, 90, 180, frameRate);
            cloudDrawer.addFrame(cloud, 100, 180, frameRate);
            cloudDrawer.addFrame(cloud, 110, 180, frameRate);
            cloudDrawer.addFrame(cloud, 120, 180, frameRate);
            cloudDrawer.addFrame(cloud, 130, 180, frameRate);
            cloudDrawer.addFrame(cloud, 140, 180, frameRate);
            cloudDrawer.addFrame(cloud, 150, 180, frameRate);

            animations = new ArrayList<>();
            animations.add(cloudDrawer);
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
