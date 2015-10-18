package com.example.pohguan.wallpaper;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

        /*
         * Time manager.
         */
        private TimeManager timeManager;

        /**
         * Constructor.
         */
        public DayNightWallpaperEngine( ){
            frameRate = 100;
            gb = new ButterflyGradientBackground();
//            setupAnimations();
            timeManager = new TimeManager();
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
            if (event.getActionMasked() == MotionEvent.ACTION_UP) {
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

            if (timeManager.testCount == 1440) timeManager.testCount = 0;
            else timeManager.testCount+=10;

            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(getResources().getColor(android.R.color.black));

                    int[] colors = gb.getCurrentGradientColor(timeManager.getHour(), timeManager.getMinute());

                    GradientDrawable grad = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
                    grad.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    //grad.setTintMode(PorterDuff.Mode.DST_IN);
//                    grad.setColorFilter(null);
                    grad.draw(canvas);

//                    if (testCount % 10 == 0 || testCount == 0)
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
            for (int i=0; i<noOfButterflies(); i++) {
                butterflyDrawer.drawFrame(canvas);
            }
        }


        /*
         * Number of butterflies.
         */
        private int noOfButterflies() {
            if (timeManager.isNoon()) {
                return 3;
            } else if (timeManager.isEvening()) {
                return 5;
            } else {
                return 4;
            }
        }


        /*
         * Setup animation.
         */
        private void setupAnimations() {
            Bitmap cloud = BitmapFactory.decodeResource(getResources(), R.drawable.butterfly);
            Bitmap sailboat = BitmapFactory.decodeResource(getResources(), R.drawable.dragonfly);

            SingleBitmapAnimationDrawer cloudDrawer = new SingleBitmapAnimationDrawer(cloud, -cloud.getWidth(), 220, 5, 0);
            SingleBitmapAnimationDrawer cloudDrawer2 = new SingleBitmapAnimationDrawer(cloud, -cloud.getWidth()*3, 420, 8, 0);
            SingleBitmapAnimationDrawer sailboatDrawer = new SingleBitmapAnimationDrawer(sailboat, -sailboat.getWidth(), 1000, 8, 0);

            animations = new ArrayList<>();
//            animations.add(cloudDrawer);
/*
            animations.add(cloudDrawer2);
            animations.add(sailboatDrawer);
*/
        }


        /*
        * Setup randomizers.
        */
        private void setupRandomizer(String choice) {
            if (choice.equals("butterflyKey")) {
                Bitmap butterfly = BitmapFactory.decodeResource(getResources(), R.drawable.butterfly);
                butterflyDrawer = new SingleBitmapRandomizerDrawer(butterfly, 0, 0, timeManager);
            } else if (choice.equals("dragonflyKey")) {
                Bitmap dragonfly = BitmapFactory.decodeResource(getResources(), R.drawable.dragonfly);
                butterflyDrawer = new SingleBitmapRandomizerDrawer(dragonfly, 0, 0, timeManager);
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
