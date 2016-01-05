package com.inventionMansion.liveWallpaper.butterfly;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by PohGuan on 4/7/2015.
 */
public class DayNightWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new DayNightWallpaperEngine();
    }

    /*private class DayNightWallpaperEngine extends Engine implements SharedPreferences.OnSharedPreferenceChangeListener  {*/
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
        *
        */
        public List<Point> myList = new ArrayList<>();

        /*
         * Time manager.
         */
        private TimeManager timeManager;

        /*
         * standard pixels squared.
         */
        private static final int stdPixelsSquared = 720*1280;

        /*
         * dbg.
         */
        private boolean dbg = false;

        /**
         * Constructor.
         */
        public DayNightWallpaperEngine( ){
            frameRate = 1600;
            gb = new ButterflyGradientBackground();
//            setupAnimations();
            timeManager = new TimeManager();
           /* SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(DayNightWallpaperService.this);
            setupRandomizer(prefs.getString(KEY_PREF_CHOICE, "butterflyKey"));*/
            handler.post(drawRunner);
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (!isPreview()) {
                // update when we hiding the view
                if (!visible)handler.post(drawRunner);
            }
/*
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(DayNightWallpaperService.this);

            if (visible) {
                prefs.unregisterOnSharedPreferenceChangeListener(this);
                handler.post(drawRunner);
            } else {
                prefs.registerOnSharedPreferenceChangeListener(this);
                handler.removeCallbacks(drawRunner);
            }
*/
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

            if (dbg || isPreview()) {
                if (timeManager.testCount == 1440) timeManager.testCount = 0;
                else timeManager.testCount += 10;
            }

            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(getResources().getColor(android.R.color.black));
                    int[] colors = gb.getCurrentGradientColor(timeManager.getHour(), timeManager.getMinute());

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
            if (dbg) handler.postDelayed(drawRunner, frameRate);
        }


        /*
         * Draw bitmaps.
         */
        private void drawBitmaps(Canvas canvas) {
            myList = new ArrayList<>();
            for (int i=0; i<animations.size(); i++) {
//                animations.get(i).randomize(canvas, myList);
                animations.get(i).drawFrame(canvas);
            }
        }


        /*
         * Draw butterflies.
         */
        private void drawButterflies(Canvas canvas) {
            int noOfButs = noOfButterflies(canvas);
            myList = new ArrayList<>();
            for (int i = 0; i < noOfButs; i++) {
                setRandomButterfly();
                butterflyDrawer.randomize(canvas, myList);
                butterflyDrawer.drawFrame(canvas);
            }
        }


        /*
         * Number of butterflies.
         */
        private int noOfButterflies(Canvas canvas) {
            float scaleFactor = (float) Math.sqrt(canvas.getHeight() * canvas.getWidth() / stdPixelsSquared);
            scaleFactor = Math.max(scaleFactor, 1);

            if (timeManager.isMorning()) {
                return (int) (5*scaleFactor);
            } else if (timeManager.isNoon() || timeManager.isAfternoon()) {
                return (int) (7*scaleFactor);
            } else if (timeManager.isEvening()) {
                return (int) (5*scaleFactor);
            }

            return (int) (10*scaleFactor);
        }


        /*
        * Setup randomizers.
        */
        private void setRandomButterfly() {
            Random rand = new Random();
            int  n = rand.nextInt(12) + 1;
            String fileKey = "but" + n;
            int id = getResources().getIdentifier(fileKey, "drawable", getPackageName());
            Bitmap butterfly = BitmapDecodeHelper.decodeSampledBitmapFromResource(getResources(), id, getRequiredWidth(), getRequiredHeight());

            if (butterfly != null) {
                butterflyDrawer = new SingleBitmapRandomizerDrawer(butterfly,timeManager);
            }
        }

        /*
         * getRequiredWidth.
         */
        private int getRequiredWidth() {
            return 50;
        }


        /*
         * getRequiredHeight.
         */
        private int getRequiredHeight() {
            return 40;
        }

        /*
      * Setup randomizers.
      */
        /*private void setupRandomizer(String choice) {
            if (choice.equals("butterflyKey")) {
                Bitmap butterfly = BitmapFactory.decodeResource(getResources(), R.drawable.but1);
                butterflyDrawer = new SingleBitmapRandomizerDrawer(butterfly);
            } else if (choice.equals("dragonflyKey")) {
                Bitmap dragonfly = BitmapFactory.decodeResource(getResources(), R.drawable.dragonfly);
                butterflyDrawer = new SingleBitmapRandomizerDrawer(dragonfly);
            } else {
                Log.d("Choice not found: ", choice);
            }
        }
*/
        /*
         * On shared preferences changed.
         */
        /*@Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(KEY_PREF_CHOICE)) {
                setupRandomizer(sharedPreferences.getString(KEY_PREF_CHOICE, "butterflyKey"));
            }
        }
*/    }
}
