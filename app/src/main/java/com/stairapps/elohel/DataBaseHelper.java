package com.stairapps.elohel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteAssetHelper {


    //TODO It never updates the database with new jokes
    //TODO We have to be careful to not delete users favorites when we update the joke database and fix the bug

    private static final String DATABASE_NAME = "jokes";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase myDataBase;

    public DataBaseHelper(Context myContext) {
        super(myContext, DATABASE_NAME, null, 1);

    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeTo = oldVersion+1;
        while(upgradeTo<=newVersion){
            switch (upgradeTo) {

            }
            upgradeTo++;

        }
    }

    //I think this method is final but I'm not sure

    public ArrayList<Joke> getJokesByCategory(String category) {
        String query;
        ArrayList<Joke> jokes = new ArrayList<>();
        if(!category.toLowerCase().contains("all")) {
             query = "SELECT * FROM jokes WHERE category=" + "'" + category + "'";
            Log.d("DBHelper","notall");
        }
        else {
            query = "SELECT * FROM jokes";
            Log.d("DBHelper","all");
        }
        Cursor c = myDataBase.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("joke")) != null) {
                jokes.add(new Joke(c.getString(c.getColumnIndex("joke")), c.getString(c.getColumnIndex("favorited")), c.getInt(c.getColumnIndex("_id"))));
            }
            c.moveToNext();
        }
        c.close();
        return jokes;
    }

    public ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        String query = "SELECT category FROM categories";

        Cursor c = myDataBase.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("category")) != null) {
                categories.add(c.getString(c.getColumnIndex("category")));
            }
            c.moveToNext();
        }
        c.close();
        return categories;
    }


    public void setFavorite(int id) {
        this.getWritableDatabase().execSQL("UPDATE jokes SET favorited = 'true' WHERE _id='" + id + "'");

    }

    public boolean isFavorited(int id) {

        Cursor c = myDataBase.rawQuery("SELECT favorited FROM jokes WHERE _id=" + id, null);
        c.moveToFirst();
        if (c.getString(c.getColumnIndex("favorited")) != null)
            return true;
        return false;
    }

    public void unFavorite(int id) {
        this.getWritableDatabase().execSQL("UPDATE jokes SET favorited = null WHERE _id='" + id + "'");
    }

    public ArrayList<Joke> getFavorites() {
        ArrayList<Joke> jokes = new ArrayList<>();
        String query = "SELECT * FROM jokes WHERE favorited = 'true'";

        Cursor c = myDataBase.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("joke")) != null) {
                jokes.add(new Joke(c.getString(c.getColumnIndex("joke")), c.getString(c.getColumnIndex("favorited")), c.getInt(c.getColumnIndex("_id"))));
            }
            c.moveToNext();
        }
        c.close();
        return jokes;
    }


    public Joke getJokeByID(int id) {

        String query = "SELECT * FROM jokes WHERE _id = " + id;
        Cursor c = myDataBase.rawQuery(query, null);
        c.moveToFirst();
        return new Joke(c.getString(c.getColumnIndex("joke")), c.getString(c.getColumnIndex("favorited")), c.getInt(c.getColumnIndex("_id")));
    }

    public void setMyDataBase(){
        myDataBase = getWritableDatabase();
    }

}
