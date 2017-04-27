package com.stairsapps.elohel.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.stairsapps.elohel.MainActivity;
import com.stairsapps.elohel.R;



public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        try {
            ((MainActivity) getActivity()).setActionBarTitle("Setari");

        }catch (Exception e){
            Intent i = new Intent(getContext(),MainActivity.class);
            startActivity(i);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
