package com.stairapps.ultimatejokessqlite.fragments;


import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
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

import com.stairapps.ultimatejokessqlite.DataBaseHelper;
import com.stairapps.ultimatejokessqlite.Joke;
import com.stairapps.ultimatejokessqlite.MainActivity;
import com.stairapps.ultimatejokessqlite.OnSwipeTouchListener;
import com.stairapps.ultimatejokessqlite.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class JokesFragment extends Fragment {


    private TextView textView;
    private DataBaseHelper DBHelper;
    private FrameLayout frameLayout;
    private int index;
    private ArrayList<Joke> jokes;
    private Menu menu;
    private boolean f;


    public JokesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jokes, container, false);
        textView = (TextView) view.findViewById(R.id.jokeText);
        frameLayout = (FrameLayout) view.findViewById(R.id.screen);

        return view;
    }

    public void databaseSetUp() {

        //Setting up the database

        //Creating a DataBaseHelper object, getting the context from the main activity
        DBHelper = new DataBaseHelper(this.getActivity());
        DBHelper.setMyDataBase();


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        databaseSetUp();


        //Setting the actionbar title for the initial screen

        ((MainActivity) getActivity()).setActionBarTitle("All");

        //Swipe and click listeners

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                nextJoke();
            }
        });
        frameLayout.setOnTouchListener(new OnSwipeTouchListener() {

            public boolean onSwipeRight() {
                nextJoke();
                return true;
            }

            public boolean onSwipeLeft() {
                previousJoke();
                return true;
            }


        });

    }

    //Setting up the menu

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
                        Log.d("JF",finalCategories[which]);
                        ((MainActivity) getActivity()).setActionBarTitle(finalCategories[which]);


                    }
                }).show();
                break;
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, textView.getText().toString() + " - Shared via UltimateJokes");
                startActivity(Intent.createChooser(shareIntent, "Share the joke"));
                break;
            case R.id.favorite:
                Bundle bundle = this.getArguments();
                int myInt = bundle.getInt("favorites", 0);

                if (DBHelper.isFavorited(jokes.get(getIndex()).getmId())) {

                    DBHelper.unFavorite(jokes.get(getIndex()).getmId());

                    if (myInt == 1) {
                        int auxIndex = index--;
                        jokes.clear();

                        //It doesn't matter here as it gets all the favorite independent of the category, it might be a bad practice but this is my current idea
                        showJokes("Punny");


                        //This is the final workaround to not go to the next joke and avoid crashes
                        //If the new index isn't in the size just go from the beginning
                        if (!(auxIndex >= 0 && auxIndex < jokes.size() - 1))
                            index = 0;
                        if (jokes.isEmpty())
                            favoritesEmpty();
                        else {
                            //I sometimes got a crash when randomly swiping through the favorites and unfavorite some jokes to get a crash where the size was 3 and the index also 3, I choose to decrease the index
                            if (auxIndex == jokes.size())
                                auxIndex--;
                            //Gets the id of the joke
                            int id = jokes.get(auxIndex).getmId();

                            //Show the joke with that id
                            showJoke(id);
                            //Making sure that the next joke is still from the current index
                            index = auxIndex;
                        }
                    } else
                    // When you unfavorite a joke the favorite icon should change
                        menu.getItem(2).setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_white_48dp));
                } else {
                    //Favorite the joke in the DB and change the menu icon
                    DBHelper.setFavorite(jokes.get(getIndex()).getmId());
                    menu.getItem(2).setIcon(getResources().getDrawable(R.drawable.ic_favorite_white_48dp));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    ;


    //Show jokes from a specific category
    public void showJokes(String category) {
        index = 0;


        Bundle bundle = this.getArguments();
        if (bundle == null) {
            Log.v("Bundle", "arguments is null ");
        } else {
            Log.v("Bundle", "text " + bundle);
        }
        int myInt = bundle.getInt("favorites", 0);
        if (myInt == 1) {
            jokes = DBHelper.getFavorites();
            ((MainActivity) getActivity()).setActionBarTitle("Favorites");
            MenuItem item = menu.findItem(R.id.sort);
            item.setVisible(false);


        } else
            jokes = DBHelper.getJokesByCategory(category);

        if (jokes.isEmpty()) {
            favoritesEmpty();
        } else {
            Collections.shuffle(jokes);
            Joke joke = jokes.get(index);

            textView.setText(unescape(joke.getmText()));
            favIconChanger(joke);
        }
    }

    //Getters and setters to access the variables from the innerclasses

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ArrayList<Joke> getJokes() {
        return jokes;
    }

    public void nextJoke() {
        //Incrementing the index
        if (index == jokes.size() - 1)
            index = 0;
        else
            index++;
        if (jokes.isEmpty()) {
            favoritesEmpty();
        } else {
            //Getting the next joke
            Joke joke = jokes.get(index);
            //Setting the textview text to the joke
            textView.setText(unescape(joke.getmText()));

            favIconChanger(joke);
        }
    }

    public void previousJoke() {
        //Decreasing the index
        if (index == 0)
            index = jokes.size() - 1;
        else
            index--;
        //Getting the previous joke
        if (jokes.isEmpty()) {
            favoritesEmpty();
        } else {
            Joke joke = jokes.get(index);
            //Setting the textview text to the joke
            textView.setText(unescape(joke.getmText()));

            favIconChanger(joke);
        }
    }

    public void favIconChanger(Joke joke) {
        //Changing the favorite icon if the joke is favorited or not
        if (DBHelper.isFavorited(joke.getmId()))
            menu.getItem(2).setIcon(getResources().getDrawable(R.drawable.ic_favorite_white_48dp));
        else
            menu.getItem(2).setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_white_48dp));
    }



    //When the favorites is empty I disable the menu buttons, the click listeners and i show the message
    public void favoritesEmpty() {
        textView.setText("You have no favorites");
        frameLayout.setOnClickListener(null);
        frameLayout.setOnTouchListener(null);
        MenuItem item = menu.findItem(R.id.favorite);
        item.setVisible(false);
        item = menu.findItem(R.id.share);
        item.setVisible(false);

    }


    public void showJoke(int id) {
        if (jokes.isEmpty()) {
            favoritesEmpty();
        } else {
            Joke joke = DBHelper.getJokeByID(id);
            textView.setText(unescape(joke.getmText()));
            favIconChanger(joke);
        }

    }

    //This is the method that fixes the \n in textview , thanks stackoverflow https://stackoverflow.com/questions/3586763/new-line-character-n-not-displaying-properly-in-textview-android/6005928#6005928
    private String unescape(String description) {
        return description.replaceAll("\\\\n", "\\\n");
    }

}
