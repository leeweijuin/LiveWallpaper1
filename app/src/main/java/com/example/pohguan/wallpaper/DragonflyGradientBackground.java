package com.example.pohguan.wallpaper;

import android.graphics.Color;

/**
 * Created by weiwei on 14/10/2015.
 */
public class DragonflyGradientBackground extends GradientBackground {

    final private int BLUE_COLOR = Color.argb(255, 32, 62, 95);
    final private int LIGHT_TURQUOSE = Color.argb(255, 57, 135, 125); //Color.argb(100, 22, 102, 120);
    final private int DARK_FORREST_GREEN = Color.argb(255, 11, 59, 43);
    final private int TURQUIOSE = Color.argb(255, 27, 81, 94);
    final private int FOREST_GREEN = Color.argb(255, 136, 185, 144);
    final private int LIME_GREEN_COLOR = Color.argb(255, 57, 91, 7);
    final private int CREAM_COLOR = Color.argb(255, 228, 225, 176); //Color.argb(255, 255, 206, 187);


    /*
   * Get current gradient color.
   */
    public int[] getCurrentGradientColor(int hourOfDay, int minOfDay) {

        System.out.println("hour" + hourOfDay + "min" + minOfDay);
        if (hourOfDay <= 3) midnightAm(hourOfDay, minOfDay);
        else if (hourOfDay <= 6) midnightDawn(hourOfDay, minOfDay);
        else if (hourOfDay <= 12) dawnMorning(hourOfDay, minOfDay);
        else if (hourOfDay <= 15) morningNoon(hourOfDay, minOfDay);
//        else if (hourOfDay <= 15) noonEvening(hourOfDay, minOfDay);
        else if (hourOfDay <= 18) eveningNight(hourOfDay, minOfDay);
        else if (hourOfDay <= 23) nightMidnight(hourOfDay, minOfDay);
        int[] gc = {top, base};

        return gc;
    }

    public void midnightAm(int hourOfDay, int minOfDay) {
        //new midnight - 3am
        double tt = 180;
        double ct = hourOfDay * 60 + minOfDay;
        double timeFactor = ct/tt;

        top = calibrateColor(LIGHT_TURQUOSE, BLUE_COLOR, top, timeFactor, hourOfDay, minOfDay);
        base = calibrateColor(BLUE_COLOR, BLUE_COLOR, base, timeFactor, hourOfDay, minOfDay);


    }

    public void midnightDawn(int hourOfDay, int minOfDay)  {
         //00 - 6am
        //new 3am - 6am
        double tt = 360;
        double ct = (hourOfDay - 3)* 60 + minOfDay;
        double timeFactor = ct/tt;

        top = calibrateColor(BLUE_COLOR, LIGHT_TURQUOSE, top, timeFactor, hourOfDay, minOfDay);
        base = calibrateColor(BLUE_COLOR, BLUE_COLOR, base, timeFactor, hourOfDay, minOfDay);
    }




    public void dawnMorning(int hourOfDay, int minOfDay) {
        //  (6am - 9am)
        // new 6am-12noon
        double tt = 360;
        double ct = (hourOfDay - 6)*60 + minOfDay;
        double timeFactor = ct/tt;

        top = calibrateColor(LIGHT_TURQUOSE, FOREST_GREEN, top, timeFactor, hourOfDay, minOfDay);
        base = calibrateColor(BLUE_COLOR, TURQUIOSE, base,timeFactor, hourOfDay, minOfDay);
    }


    public void morningNoon(int hourOfDay, int minOfDay) {
        //target white to pink (9am - 12noon)
        // new 12noon - 3pm
        double tt = 180;
        double ct = (hourOfDay - 12)*60 + minOfDay;
        double timeFactor = ct/tt;

        top = calibrateColor(FOREST_GREEN, CREAM_COLOR, top, timeFactor, hourOfDay, minOfDay);
        base = calibrateColor(TURQUIOSE, LIGHT_TURQUOSE, base,timeFactor, hourOfDay, minOfDay);
    }


/*
    public void noonEvening(int hourOfDay, int minOfDay) {
        //12 noon - 3pm
        //
        double tt = 180;
        double ct = (hourOfDay - 12)*60 + minOfDay;
        double timeFactor = ct/tt;

        top = calibrateColor(CREAM_COLOR, CREAM_COLOR, top, timeFactor, hourOfDay, minOfDay);
        base = calibrateColor(LIGHT_TURQUOSE, DARK_FORREST_GREEN, base,timeFactor, hourOfDay, minOfDay);
    }
*/



    public void eveningNight(int hourOfDay, int minOfDay) {
        //target purple to darkblue. (3pm - 6pm)
        // new 3pm-6pm
        double tt = 180;
        double ct = (hourOfDay - 15)*60 + minOfDay;
        double timeFactor = ct/tt;

        top = calibrateColor(CREAM_COLOR, FOREST_GREEN, top, timeFactor, hourOfDay, minOfDay);
        base = calibrateColor(LIGHT_TURQUOSE,LIGHT_TURQUOSE, base, timeFactor, hourOfDay, minOfDay);
    }


    public void nightMidnight(int hourOfDay, int minOfDay) {
        //target dark blue to darkblue , 6pm - midnight
        //new 6pm - midnight
        double tt = 360;
        double ct = (hourOfDay - 18)* 60 + minOfDay;
        double timeFactor = ct/tt;

        top = calibrateColor(FOREST_GREEN, LIGHT_TURQUOSE, top, timeFactor, hourOfDay, minOfDay);
        base = calibrateColor(LIGHT_TURQUOSE, BLUE_COLOR, base, timeFactor, hourOfDay, minOfDay);
    }


    /*
    * Calibrate color.
    */
    public int calibrateColor(int previousColor, int targetColor, int currentColor, double timeFactor,
                              int hourOfDay, int minOfDay) {
        int redDiff = Math.abs(Color.red(targetColor) - Color.red(previousColor));
        int blueDiff = Math.abs(Color.blue(targetColor) - Color.blue(previousColor));
        int greenDiff = Math.abs(Color.green(targetColor) - Color.green(previousColor));
        int newBlue = Color.blue(previousColor);
        int newGreen = Color.green(previousColor);
        int newRed = Color.red(previousColor);

        int increment =  (int)Math.floor(timeFactor * (redDiff + blueDiff + greenDiff));

        if (Color.blue(currentColor) != Color.blue(targetColor)) {
            newBlue = calibrateSubColor(Color.blue(previousColor), Color.blue(targetColor),
                    Color.blue(currentColor), increment);
        } else if (Color.blue(currentColor) == Color.blue(targetColor)){
            newBlue = Color.blue(targetColor);
        }

        if (Color.green(currentColor) != Color.green(targetColor) && (increment - blueDiff >= 0)) {
            newGreen = calibrateSubColor(Color.green(previousColor), Color.green(targetColor),
                    Color.green(currentColor), Math.max(0, (increment - blueDiff)));
        } else if (Color.green(currentColor) == Color.green(targetColor)) {
            newGreen = Color.green(targetColor);
        }

        if (Color.red(currentColor) != Color.red(targetColor) && (increment - greenDiff - blueDiff) >= 0) {
            newRed = calibrateSubColor(Color.red(previousColor), Color.red(targetColor),
                    Color.red(currentColor), Math.max(0, (increment - greenDiff - blueDiff)));
        } else if (Color.red(currentColor) == Color.red(targetColor)){
            newRed = Color.red(targetColor);
        }

        return Color.argb(getAlpha(hourOfDay, minOfDay), newRed, newGreen, newBlue);

    }

}
