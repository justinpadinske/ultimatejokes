package com.stairapps.ultimatejokessqlite.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.stairapps.ultimatejokessqlite.DataBaseHelper;
import com.stairapps.ultimatejokessqlite.Joke;
import com.stairapps.ultimatejokessqlite.MainActivity;
import com.stairapps.ultimatejokessqlite.R;
import com.stairapps.ultimatejokessqlite.RVAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class JokesLV extends Fragment {

    private DataBaseHelper DBHelper;
    private RecyclerView rv;
    private ArrayList<Joke> jokes;
    private Menu menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jokes_lv, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        databaseSetUp();
        ((MainActivity) getActivity()).setActionBarTitle("All");

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);



    }

    public void databaseSetUp() {
        //Setting up the database
        //Creating a DataBaseHelper object, getting the context from the main activity
        DBHelper = new DataBaseHelper(this.getActivity());
        DBHelper.setMyDataBase();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_jokes, menu);
        this.menu = menu;
        //I need to use the showJokes method here to make sure it is ran after the menu is created
        showJokes("All");
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.favorite);
        item.setVisible(false);
        item = menu.findItem(R.id.share);
        item.setVisible(false);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                //Getting the categories from the DB
                String[] categories = new String[DBHelper.getCategories().size()];
                categories = DBHelper.getCategories().toArray(categories);
                //Creating the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select a category");
                final String[] finalCategories = categories;
                builder.setItems(categories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(getActivity(), finalCategories[which],Toast.LENGTH_SHORT).show();
                        showJokes(finalCategories[which]);
                        Log.d("JF", finalCategories[which]);
                        ((MainActivity) getActivity()).setActionBarTitle(finalCategories[which]);


                    }
                }).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    ;


    //Show jokes from a specific category
    public void showJokes(String category) {
            jokes = DBHelper.getJokesByCategory(category);
            Collections.shuffle(jokes); //This is done to always have a new order of the jokes
            RVAdapter adapter=new RVAdapter(jokes);//Creating the adapter for the RecylcerView
            rv.setAdapter(adapter);
        }



}
