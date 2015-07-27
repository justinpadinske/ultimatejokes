package com.stairapps.ultimatejokessqlite;


import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

        index=0;
        jokes = DBHelper.getJokesByCategory("Punny");


        textView.setText(jokes.get(index));

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
                JokesFragment.this.setIndex(getIndex()-1);
                JokesFragment.this.textView.setText(getJokes().get(getIndex()));
                return true;
            }


        });

    }

  ;

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
