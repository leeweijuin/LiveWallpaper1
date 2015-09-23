package com.example.pohguan.wallpaper;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

/**
 * Created by PohGuan on 12/7/2015.
 */
public class WallpaperPreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  /*    //This can be used if you don't prefer the header version
         getFragmentManager().beginTransaction().replace(android.R.id.content,
                new GraphicPrefFragment()).commit();
  */  }


    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    /**
     * This fragment shows the preferences for the first header.
     */
    public static class GraphicPrefFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.
/*
            PreferenceManager.setDefaultValues(getActivity(),
                    R.xml.prefs, false);
*/

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.prefs);
        }
    }


    @Override
    public boolean isValidFragment(String s) {
        return GraphicPrefFragment.class.getName().equals(s);
    }

}
