package com.stairsapps.elohel.database;

/**
 * Created by filip on 9/2/15.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.stairsapps.elohel.Joke;

import java.sql.SQLException;
import java.util.ArrayList;



/**
 * Created by filip on 8/30/15.
 */
public class SQLController {

    private DatabaseHelper DBHelper;
    private Context context;
    private SQLiteDatabase database;

    public SQLController(Context c){
        this.context = c;
    }

    public SQLController open() throws SQLException {
        DBHelper = DBHelper.getInstance(context);
        database = DBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        DBHelper.close();
    }

    public Cursor getJokes(String category) {
        String query;

        if(!category.equals("All")) {
            query = "SELECT * FROM jokes WHERE category=" + "'" + category + "'";
        }
        else {
            query = "SELECT * FROM jokes";
        }
        Cursor c = database.rawQuery(query, null);
        if(c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public ArrayList<String> getCategories() {
        String query = "SELECT category FROM categories";
        ArrayList<String> categories = new ArrayList<>();
        Cursor c = database.rawQuery(query, null);
        if(c!=null) {
            c.moveToFirst();
        }
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
        database.execSQL("UPDATE jokes SET favorite = 'true' WHERE _id='" + id + "'");
    }

    public void unFavorite(int id) {
        database.execSQL("UPDATE jokes SET favorite = 'false' WHERE _id='" + id + "'");
    }

    public boolean isFavorited(int id) {
        Cursor c = database.rawQuery("SELECT favorite FROM jokes WHERE _id=" + id, null);
        c.moveToFirst();
        if (c.getString(c.getColumnIndex("favorite")).equals("true")){
            c.close();
            return true;
        }
        c.close();
        return false;

    }

    public Cursor getFavorites() {
        ArrayList<Joke> jokes = new ArrayList<>();
        String query = "SELECT * FROM jokes WHERE favorite = 'true'";

        Cursor c = database.rawQuery(query, null);
        if (c !=null) {
            c.moveToFirst();
        }

        return c;
    }

    public Joke getJoke(int id) {

        String query = "SELECT * FROM jokes WHERE _id = " + id;
        Log.i("DB", String.valueOf(database.isOpen()));
        Cursor c = database.rawQuery(query, null);
        c.moveToFirst();
        Joke joke = new Joke();
        joke.setId(id);
        joke.setCategory(c.getString(c.getColumnIndex("category")));
        joke.setFavoriteStatus(this.isFavorited(id));
        joke.setJoke(c.getString(c.getColumnIndex("joke")));
        return joke;
    }


    public int getMaxId()
    {
        String query = "SELECT MAX(_id) AS max_id FROM jokes";
        Cursor cursor = database.rawQuery(query,null);

        int id = 0;
        if (cursor.moveToFirst())
        {
            do
            {
                id = cursor.getInt(0);
            } while(cursor.moveToNext());
        }
        return id;
    }



}
