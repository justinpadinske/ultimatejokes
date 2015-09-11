package com.stairsapps.elohel.fragments;

import android.os.Bundle;


import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.stairsapps.elohel.R;



public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }




}
