package com.stairapps.elohel;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by filip on 14.07.2015.
 */
public class ApplicationClass extends Application {

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
