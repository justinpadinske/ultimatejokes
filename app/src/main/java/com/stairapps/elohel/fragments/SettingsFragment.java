package com.stairapps.elohel.fragments;

import android.os.Bundle;


import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.stairapps.elohel.R;



public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }


}
