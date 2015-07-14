package com.stairapps.ultimatejokessqlite;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private DataBaseHelper DBHelper;
    private Drawer result = null;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp(savedInstanceState);
        databaseSetUp();
        manager = getSupportFragmentManager();

        JokesFragment jokesFragment = new JokesFragment();

        if(savedInstanceState==null)
        manager.beginTransaction().add(R.id.fragment_container,jokesFragment).addToBackStack(null).commit();



    }

    public void databaseSetUp(){

        DBHelper = new DataBaseHelper(this);
        try{
            DBHelper.createDataBase();
        } catch (IOException e) {
            throw new Error("Unable to create database");
        }
        try {
            DBHelper.openDataBase();
        }catch (SQLiteException sqle){
            throw sqle;
        }


    }

    public void setUp(Bundle state){

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


         result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Jokes").withIdentifier(0).withIcon(R.drawable.ic_home_black_48dp),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Pictures").withIdentifier(1).withIcon(R.drawable.ic_insert_photo_black_48dp),
                        new PrimaryDrawerItem().withName("Favorites").withIdentifier(2).withIcon(R.drawable.ic_favorite_black_48dp),
                        new PrimaryDrawerItem().withName("Settings").withIdentifier(3).withIcon(R.drawable.ic_settings_black_48dp)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        switch (drawerItem.getIdentifier()) {
                            case 0:
                                manager.beginTransaction().replace(R.id.fragment_container,new JokesFragment()).commit();
                                break;
                            case 1:
                                manager.beginTransaction().replace(R.id.fragment_container, new PicturesFragment()).commit();
                                break;
                            case 2:
                                //TODO IMPLEMENT THIS, I DON'T WANT TO USE ANOTHER FRAGMENT
                                manager.beginTransaction().replace(R.id.fragment_container, new JokesFragment()).commit();
                                break;
                            case 3:
                                manager.beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                                break;
                            default:
                                manager.beginTransaction().replace(R.id.fragment_container, new JokesFragment()).commit();
                                break;

                        }
                        return true;
                    }
                })
                .withShowDrawerOnFirstLaunch(true)
                .withSavedInstance(state)
                .build();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
