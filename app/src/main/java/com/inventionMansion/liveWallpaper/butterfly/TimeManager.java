package com.inventionMansion.liveWallpaper.butterfly;

import java.util.Calendar;

/**
 * Created by PohGuan on 10/18/2015.
 */
public class TimeManager {

    /*
     * The calendar.
     */
    private Calendar calendar;

    /*
    * Test count.
    */
    public int testCount;

    /*
     * Debug mode.
     */
    private static final boolean dbgMode = false;


    public TimeManager() {
        calendar = Calendar.getInstance();
    }


    /*
 * Get hour.
 */
    public int getHour() {
        if (dbgMode) {
            return testCount/60;
        }

        Calendar c = Calendar.getInstance();
        return c.get(Calendar.HOUR_OF_DAY);
    }


    /*
    * Get minute.
    */
    public int getMinute() {
        if (dbgMode) {
            return testCount % 60;
        }
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MINUTE);
    }


    /*
     * Is Morning.
     */
    public boolean isMorning() {
        if (getHour() >= 7 && getHour() <= 10) {
            return true;
        }
        return false;
    }

    /*
     * Is Noon.
     */
    public boolean isNoon() {
        if (getHour() >= 11 && getHour() <= 13) {
            return true;
        }
        return false;
    }

    /*
     * Is Afternonn.
     */
    public boolean isAfternoon() {
        if (getHour() >= 14 && getHour() <= 18) {
            return true;
        }
        return false;
    }

    /*
     * Is Evening.
     */
    public boolean isEvening() {
        if (getHour() >= 19 || getHour() <= 7) {
            return true;
        }
        return false;
    }

}
