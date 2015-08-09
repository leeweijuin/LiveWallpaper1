package com.example.pohguan.wallpaper;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by PohGuan on 3/7/2015.
 */
public class WallpaperPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs);
    }

}
