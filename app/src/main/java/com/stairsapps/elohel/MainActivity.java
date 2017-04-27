package com.stairsapps.elohel;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.stairsapps.elohel.database.SQLController;
import com.stairsapps.elohel.extra.ApplicationClass;
import com.stairsapps.elohel.fragments.FavoritesFragment;
import com.stairsapps.elohel.fragments.JokePurpose;
import com.stairsapps.elohel.fragments.JokesLV;
import com.stairsapps.elohel.fragments.JokesSimple;
import com.stairsapps.elohel.fragments.SettingsFragment;


import java.sql.SQLException;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private Drawer result = null;
    private FragmentManager manager;
    private SharedPreferences sharedPreferences;
    private boolean listMode;


    @Override
    protected void onResume() {
        super.onResume();
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setUp(savedInstanceState);


        manager = getSupportFragmentManager();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        /**
         * Showing the home screen
         */
        Fragment a;
        if(listMode) {
            a = new JokesLV();
        }else {
            a = new JokesSimple();
        }

        if (savedInstanceState == null)
            manager.beginTransaction().add(R.id.fragment_container,a,"HOME").addToBackStack(null).commit();


    }


    public void setUp(Bundle state) {



        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Glume").withIdentifier(0).withIcon(R.drawable.ic_home_black_48dp),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Favorite").withIdentifier(1).withIcon(R.drawable.ic_favorite_black_48dp),
                        new PrimaryDrawerItem().withName("Propune o gluma").withIdentifier(2).withIcon(R.drawable.ic_insert_emoticon_black_48dp),
                        new PrimaryDrawerItem().withName("Setari").withIdentifier(3).withIcon(R.drawable.ic_settings_black_48dp)
        )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        switch (drawerItem.getIdentifier()) {
                            case 0:
                                updateListMode();
                                Fragment a;
                                if(listMode) {
                                    a = new JokesLV();
                                }else {
                                    a = new JokesSimple();
                                }
                                manager.beginTransaction().replace(R.id.fragment_container, a,"HOME").commit();
                                break;
                            case 1:
                                updateListMode();
                                FavoritesFragment j = new FavoritesFragment();
                                manager.beginTransaction().replace(R.id.fragment_container, j).commit();
                                break;
                            case 2:
                                manager.beginTransaction().replace(R.id.fragment_container,new JokePurpose()).commit();
                                break;
                            case  3:
                                manager.beginTransaction().replace(R.id.fragment_container,new SettingsFragment()).commit();
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

    /**
     * Implemented the code to close the drawer on back press
     */
    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            Fragment jk = manager.findFragmentByTag("HOME");
            if(jk!=null && jk.isVisible()) {
                finish();
            }
            else {
                manager.beginTransaction().replace(R.id.fragment_container, new JokesSimple(),"HOME").commit();
            }

        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    /**
     * Setting up the action bar
     *
     */
    public void setActionBarTitle(String title){
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    public void updateListMode(){
        listMode = sharedPreferences.getBoolean("list_mode",true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private String unescape(String description) {
        return description.replaceAll("\\\\n", "\\\n");
    }

}
