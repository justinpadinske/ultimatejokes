package com.stairapps.elohel.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.stairapps.elohel.Joke;
import com.stairapps.elohel.MainActivity;
import com.stairapps.elohel.R;
import com.stairapps.elohel.adapters.RVAdapter;
import com.stairapps.elohel.database.SQLController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class JokesLV extends Fragment {

    protected SQLController dbcon;
    protected RecyclerView rv;
    protected ArrayList<Joke> jokes;
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
        //Creating a DatabaseHelper object, getting the context from the main activity
        dbcon = new SQLController(getActivity());
        try {
            dbcon.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


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
                String[] categories = new String[dbcon.getCategories().size()];
                categories = dbcon.getCategories().toArray(categories);
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
            jokes = new ArrayList<>();
            parseJokes(dbcon.getJokes(category));
            RVAdapter adapter=new RVAdapter(jokes,dbcon);//Creating the adapter for the RecylcerView
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter);
        scaleInAnimationAdapter.setFirstOnly(false);
            rv.setAdapter(scaleInAnimationAdapter);

        }

    public void parseJokes(Cursor c){
        jokes.clear();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("joke")) != null) {
                Joke joke = new Joke();
                joke.setId(c.getInt(c.getColumnIndex("_id")));
                joke.setCategory(c.getString(c.getColumnIndex("category")));
                joke.setFavoriteStatus(dbcon.isFavorited(joke.getId()));
                joke.setJoke(c.getString(c.getColumnIndex("joke")));
                jokes.add(joke);
            }
            c.moveToNext();
        }
        c.close();
        Collections.shuffle(jokes);
    }


}
