package com.stairapps.elohel;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.stairapps.elohel.fragments.FavoritesFragment;
import com.stairapps.elohel.fragments.JokePurpose;
import com.stairapps.elohel.fragments.JokesLV;
import com.stairapps.elohel.fragments.JokesSimple;
import com.stairapps.elohel.fragments.SettingsFragment;


import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;

    private Drawer result = null;
    //I'm not sure if we should use the support library or not
    private android.app.FragmentManager manager;
    private SharedPreferences sharedPreferences;
    private boolean listMode;
    /**
     * I use a bundle and pass an extra int when switching fragments to check if the users want the favorites or all the jokes
     * You can see it belown in the setUp method
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp(savedInstanceState);

        manager = getFragmentManager();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        listMode = sharedPreferences.getBoolean("list_mode",true);
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
                        new PrimaryDrawerItem().withName("Jokes").withIdentifier(0).withIcon(R.drawable.ic_home_black_48dp),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Favorites").withIdentifier(1).withIcon(R.drawable.ic_favorite_black_48dp),
                        new PrimaryDrawerItem().withName("Purpose a joke").withIdentifier(2).withIcon(R.drawable.emoticon),
                        new PrimaryDrawerItem().withName("Settings").withIdentifier(3).withIcon(R.drawable.ic_settings_black_48dp)
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

/*
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
*/

    /**
     * Implemented the code to close the drawer on back press
     */
    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            JokesSimple jk = (JokesSimple) manager.findFragmentByTag("HOME");
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



}
