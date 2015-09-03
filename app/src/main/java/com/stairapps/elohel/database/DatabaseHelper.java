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

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

}
