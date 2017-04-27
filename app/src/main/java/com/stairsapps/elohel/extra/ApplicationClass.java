package com.stairsapps.elohel.extra;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.stairsapps.elohel.R;


import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by filip on 14.07.2015.
 */
public class ApplicationClass extends Application {


    ;


    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

    }





}
