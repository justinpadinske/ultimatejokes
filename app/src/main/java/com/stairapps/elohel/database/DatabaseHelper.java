package com.stairapps.elohel.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.stairapps.elohel.Joke;

import java.util.ArrayList;

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
