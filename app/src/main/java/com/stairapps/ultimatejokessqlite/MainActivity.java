package com.stairapps.ultimatejokessqlite;


import android.content.Context;
import android.support.v4.app.FragmentManager;
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
import com.stairapps.ultimatejokessqlite.fragments.JokesFragment;
import com.stairapps.ultimatejokessqlite.fragments.PicturesFragment;
import com.stairapps.ultimatejokessqlite.fragments.SettingsFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;

    private Drawer result = null;
    private FragmentManager manager;

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp(savedInstanceState);

        manager = getSupportFragmentManager();

        JokesFragment jokesFragment = new JokesFragment();

        Bundle argsa= new Bundle();
        argsa.putInt("favorites", 0);
        jokesFragment.setArguments(argsa);
        if (savedInstanceState == null)
            manager.beginTransaction().add(R.id.fragment_container, jokesFragment,"HOME").addToBackStack(null).commit();


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
                        new PrimaryDrawerItem().withName("Pictures").withIdentifier(1).withIcon(R.drawable.ic_insert_photo_black_48dp),
                        new PrimaryDrawerItem().withName("Favorites").withIdentifier(2).withIcon(R.drawable.ic_favorite_black_48dp),
                        new PrimaryDrawerItem().withName("Settings").withIdentifier(3).withIcon(R.drawable.ic_settings_black_48dp)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        switch (drawerItem.getIdentifier()) {
                            case 0:
                                JokesFragment a = new JokesFragment();
                                Bundle argsa= new Bundle();
                                argsa.putInt("favorites",0);
                                a.setArguments(argsa);
                                manager.beginTransaction().replace(R.id.fragment_container, a,"HOME").commit();
                                break;
                            case 1:
                                manager.beginTransaction().replace(R.id.fragment_container, new PicturesFragment()).commit();
                                break;
                            case 2:
                                //TODO IMPLEMENT THIS, I DON'T WANT TO USE ANOTHER FRAGMENT

                                Bundle args = new Bundle();
                                args.putInt("favorites", 1);
                                JokesFragment j = new JokesFragment();
                                j.setArguments(args);
                                manager.beginTransaction().replace(R.id.fragment_container, j).commit();
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
            JokesFragment jk = (JokesFragment) manager.findFragmentByTag("HOME");
            if(jk!=null && jk.isVisible()) {
                finish();
            }
            else {
                manager.beginTransaction().replace(R.id.fragment_container, new JokesFragment(),"HOME").commit();
            }

        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
    }

}
