package com.inventionMansion.liveWallpaper.butterfly;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * Created by PohGuan on 5/7/2015.
 */

public class SetWallpaperActivity extends Activity {
    final private int cRequestSetWallpaper = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, DayNightWallpaperService.class));
        PreferenceManager.setDefaultValues(this,
                R.xml.prefs, false);
        startActivityForResult(intent, cRequestSetWallpaper);
    }

    public void onClick(View view) {
        /*Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, DayNightWallpaperService.class));
        startActivity(intent);*/
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == cRequestSetWallpaper) {
            finish();
        }
    }
}
