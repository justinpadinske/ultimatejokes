package com.stairsapps.elohel.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper {


    private static final String DATABASE_NAME = "jokes.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper sInstance;
    public static synchronized DatabaseHelper getInstance(Context context){
        if(sInstance == null){
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

}
