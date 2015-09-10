package com.stairapps.elohel;


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
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.stairapps.elohel.database.SQLController;
import com.stairapps.elohel.fragments.FavoritesFragment;
import com.stairapps.elohel.fragments.JokePurpose;
import com.stairapps.elohel.fragments.JokesLV;
import com.stairapps.elohel.fragments.JokesSimple;
import com.stairapps.elohel.fragments.SettingsFragment;


import java.sql.SQLException;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;

    private Drawer result = null;
    //I'm not sure if we should use the support library or not
    private FragmentManager manager;
    private SharedPreferences sharedPreferences;
    private boolean listMode;


    //used for register alarm manager
    PendingIntent pendingIntent;
    //used to store running alarmmanager instance
    AlarmManager alarmManager;
    //Callback function for Alarmmanager event
    BroadcastReceiver mReceiver;

    /**
     * I use a bundle and pass an extra int when switching fragments to check if the users want the favorites or all the jokes
     * You can see it belown in the setUp method
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp(savedInstanceState);

        //RegisterAlarmBroadcast();


        manager = getSupportFragmentManager();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        listMode = sharedPreferences.getBoolean("list_mode",true);
        /**
         * Showing the home screen
         */
     //x   alarmManager.set( AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000 , pendingIntent );


        Fragment a;
        if(listMode) {
            a = new JokesLV();
        }else {
            a = new JokesSimple();
        }

        if (savedInstanceState == null)
            manager.beginTransaction().add(R.id.fragment_container,a,"HOME").addToBackStack(null).commit();


    }

    private void RegisterAlarmBroadcast() {

        IntentFilter filter = new IntentFilter("com.stairsapps.elohel.alarm");
        filter.addAction("FAVORITE");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SQLController helper = new SQLController(context);
                try {
                    helper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Joke joke = helper.getJoke(new Random().nextInt(helper.getMaxId()));

                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                Intent resultIntent = new Intent(context, MainActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(MainActivity.class);



                NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
                bigStyle.setBigContentTitle(joke.getCategory());
                bigStyle.bigText(unescape(joke.getJoke()));

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle("El Oh El").setAutoCancel(true)
                                .setPriority(Notification.PRIORITY_LOW)
                                .setStyle(bigStyle)
                                .addAction(R.drawable.ic_favorite_black_48dp,"Favorite",pendingIntent)
                                .setContentText(unescape(joke.getJoke()));


                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);

                if(intent.getAction().equals("FAVORITE"))
                    Log.i("tag","fa");
                else
                    Log.i("tag","no");
                mNotificationManager.notify(999, mBuilder.build());




            }
        };

        registerReceiver(mReceiver,filter);
        pendingIntent = PendingIntent.getBroadcast(this,0,new Intent("com.stairsapps.elohel.alarm"),0);
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);


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

    private void UnregisterAlarmBroadcast()
    {
        alarmManager.cancel(pendingIntent);
        getBaseContext().unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
//        unregisterReceiver(mReceiver);
        super.onDestroy();

    }

    private String unescape(String description) {
        return description.replaceAll("\\\\n", "\\\n");
    }

}
