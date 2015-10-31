package com.stairsapps.elohel.fragments;

import android.content.Intent;
import android.os.Bundle;


import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.stairsapps.elohel.MainActivity;
import com.stairsapps.elohel.R;



public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        try {
            ((MainActivity) getActivity()).setActionBarTitle("Settings");

        }catch (Exception e){
            Intent i = new Intent(getContext(),MainActivity.class);
            startActivity(i);
        }
    }




}
