package com.example.pohguan.wallpaper;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by PohGuan on 4/7/2015.
 */
public class DayNightWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new DayNightWallpaperEngine();
    }

    private class DayNightWallpaperEngine extends Engine implements SharedPreferences.OnSharedPreferenceChangeListener  {
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
        * gradient background.
        */
        private GradientBackground gb;

        /*
        * Test count.
        */
        private int testCount;

        /*
         * Animation drawers
         */
        private List<BitmapAnimationDrawer> animations;

        /*
         * Randomizer drawers
         */
        private SingleBitmapRandomizerDrawer butterflyDrawer;

        /*
         * pattern key.
         */
        public static final String KEY_PREF_CHOICE = "choice";

        /**
         * Constructor.
         */
        public DayNightWallpaperEngine( ){
            frameRate = 100;
            gb = new ButterflyGradientBackground();
//            setupAnimations();
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(DayNightWallpaperService.this);
            setupRandomizer(prefs.getString(KEY_PREF_CHOICE, "butterflyKey"));
            handler.post(drawRunner);
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(DayNightWallpaperService.this);

            if (visible) {
                prefs.unregisterOnSharedPreferenceChangeListener(this);
                handler.post(drawRunner);
            } else {
                prefs.registerOnSharedPreferenceChangeListener(this);
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


        /*
         * Get hour.
         */
        public int getHour() {
            Calendar c = Calendar.getInstance();
            return testCount/60;
        }


        /*
        * Get minute.
        */
        public int getMinute() {
            return testCount % 60;
        }

        /**
         * Draw generic.
         */
        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            if (testCount == 1440) testCount = 0;
            else testCount+=10;

            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    //canvas.drawColor(getResources().getColor(android.R.color.black));
                    canvas.drawColor(Color.argb(gb.getAlpha(getHour(), getMinute()), 0, 0, 0));
                    int[] colors = gb.getCurrentGradientColor(getHour(), getMinute());

                    GradientDrawable grad = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
                    grad.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    grad.draw(canvas);

                    drawButterflies(canvas);
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
         * Draw butterflies.
         */
        private void drawButterflies(Canvas canvas) {
            butterflyDrawer.drawFrame(canvas);
        }


        /*
         * Setup animation.
         */
        private void setupAnimations() {
            Bitmap cloud = BitmapFactory.decodeResource(getResources(), R.drawable.butterfly);
            Bitmap sailboat = BitmapFactory.decodeResource(getResources(), R.drawable.sailboat);

            SingleBitmapAnimationDrawer cloudDrawer = new SingleBitmapAnimationDrawer(cloud, -cloud.getWidth(), 220, 5, 0);
            SingleBitmapAnimationDrawer cloudDrawer2 = new SingleBitmapAnimationDrawer(cloud, -cloud.getWidth()*3, 420, 8, 0);
            SingleBitmapAnimationDrawer sailboatDrawer = new SingleBitmapAnimationDrawer(sailboat, -sailboat.getWidth(), 1000, 8, 0);

            animations = new ArrayList<>();
            animations.add(cloudDrawer);
            animations.add(cloudDrawer2);
            animations.add(sailboatDrawer);
        }


        /*
        * Setup randomizers.
        */
        private void setupRandomizer(String choice) {
            if (choice.equals("butterflyKey")) {
                Bitmap butterfly = BitmapFactory.decodeResource(getResources(), R.drawable.butterfly);
                butterflyDrawer = new SingleBitmapRandomizerDrawer(butterfly, 0, 0);
            } else if (choice.equals("dragonflyKey")) {
                Bitmap dragonfly = BitmapFactory.decodeResource(getResources(), R.drawable.dragonfly);
                butterflyDrawer = new SingleBitmapRandomizerDrawer(dragonfly, 0, 0);
            } else {
                Log.d("Choice not found: ", choice);
            }
        }


        /*
         * On shared preferences changed.
         */
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(KEY_PREF_CHOICE)) {
                setupRandomizer(sharedPreferences.getString(KEY_PREF_CHOICE, "butterflyKey"));
            }
        }
    }
}
