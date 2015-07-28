package com.stairapps.ultimatejokessqlite;


import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ShareActionProvider;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.IOException;
import java.util.ArrayList;


public class JokesFragment extends Fragment {



    private TextView textView;
    private DataBaseHelper DBHelper;
    private FrameLayout frameLayout;
    private int index;
    private ArrayList<String> jokes;



    public JokesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_jokes, container, false);
        textView = (TextView) view.findViewById(R.id.jokeText);
        frameLayout = (FrameLayout) view.findViewById(R.id.screen);

        return view;
    }

    public void databaseSetUp() {

        DBHelper = new DataBaseHelper(this.getActivity());
        try {
            DBHelper.createDataBase();
        } catch (IOException e) {
            throw new Error("Unable to create database");
        }
        try {
            DBHelper.openDataBase();
        } catch (SQLiteException sqle) {
            throw sqle;
        }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        databaseSetUp();

        //Default jokes category
        showJokes(DBHelper.getCategories().get(0));

        //Swipe and click listeners

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                JokesFragment.this.setIndex(getIndex()+1);
                JokesFragment.this.textView.setText(getJokes().get(getIndex()));
            }
        });
        frameLayout.setOnTouchListener(new OnSwipeTouchListener() {

            public boolean onSwipeRight() {
                JokesFragment.this.setIndex(getIndex() + 1);
                JokesFragment.this.textView.setText(getJokes().get(getIndex()));
                return true;
            }

            public boolean onSwipeLeft() {
                JokesFragment.this.setIndex(getIndex() - 1);
                JokesFragment.this.textView.setText(getJokes().get(getIndex()));
                return true;
            }


        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_jokes, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort:
                String[] categories = new String[DBHelper.getCategories().size()];
                categories = DBHelper.getCategories().toArray(categories);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select a category");
                final String[] finalCategories = categories;
                builder.setItems(categories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), finalCategories[which],Toast.LENGTH_SHORT).show();
                        showJokes(finalCategories[which]);
                    }
                }).show();
                break;
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,textView.getText().toString()+" - Shared via UltimateJokes");
                startActivity(Intent.createChooser(shareIntent,"Share the joke"));
                break;
            case R.id.favorite:break;
        }
        return super.onOptionsItemSelected(item);
    }

    ;

    public void showJokes(String category){
        index=0;
        jokes = DBHelper.getJokesByCategory(category);
        textView.setText(jokes.get(index));
    }

    //Getters and setters to access the variables from the innerclasses

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ArrayList<String> getJokes() {
        return jokes;
    }
}
