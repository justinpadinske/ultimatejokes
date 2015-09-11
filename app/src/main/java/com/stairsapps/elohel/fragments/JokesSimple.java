package com.stairsapps.elohel.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ShareEvent;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.stairsapps.elohel.Joke;
import com.stairsapps.elohel.MainActivity;
import com.stairsapps.elohel.R;
import com.stairsapps.elohel.database.SQLController;
import com.stairsapps.elohel.extra.ApplicationClass;
import com.stairsapps.elohel.extra.OnSwipeTouchListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;


public class JokesSimple extends Fragment {

    private ArrayList<Joke> mJokes;
    private int mPosition = 0;
    private TextSwitcher mTextSwitcher;
    private SQLController dbcon;
    private RelativeLayout relativeLayout;
    private Menu menu;
    private ProgressBar mProgress;
    private Tracker mTracker;

//    private String TAG = getActivity().getPackageName();

    public JokesSimple() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ApplicationClass app = (ApplicationClass) getActivity().getApplication();
        mTracker = app.getDefaultTracker();
        mTracker.setScreenName("Jokes Simple");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle("All");


        //Connect to the database
        dbcon = new SQLController(getActivity());
        try {
            dbcon.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Get the jokes
        mJokes = new ArrayList<>();
        Cursor c = dbcon.getJokes("All");
        parseJokes(c);

        //Set up the listeners

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNext();
            }
        });
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener() {
            @Override
            public boolean onSwipeRight() {
                onNext();
                return true;
            }

            @Override
            public boolean onSwipeLeft() {
                onPrevious();
                return true;
            }
        });


        mTextSwitcher.setInAnimation(getActivity(), android.R.anim.fade_in);
        mTextSwitcher.setOutAnimation(getActivity(), android.R.anim.fade_out);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jokes,container,false);
        //Setting up the TextSwitcher

        mTextSwitcher = (TextSwitcher) view.findViewById(R.id.textSwitcher);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        mProgress = (ProgressBar) view.findViewById(R.id.myProgress);
        return view;
    }

    public void parseJokes(Cursor c){
        mJokes.clear();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("joke")) != null) {
                Joke joke = new Joke();
                joke.setId(c.getInt(c.getColumnIndex("_id")));
                joke.setCategory(c.getString(c.getColumnIndex("category")));
                joke.setFavoriteStatus(dbcon.isFavorited(joke.getId()));
                joke.setJoke(c.getString(c.getColumnIndex("joke")));
                mJokes.add(joke);
            }
            c.moveToNext();
        }
        c.close();
        Collections.shuffle(mJokes);
    }


    //Switchers
    //Change to next joke
    public void onNext() {
        if(mPosition<mJokes.size()-1){
            mPosition++;
        }
        else {
            mPosition=0;
        }
        mTextSwitcher.setText(unescape(mJokes.get(mPosition).getJoke()));
        setFavoriteIcon();
        mProgress.setProgress(mPosition);
    }

    //Change to previous joke

    public void onPrevious() {
        if(mPosition>0){
            mPosition--;
        }
        else {
            mPosition=mJokes.size()-1;
        }
        mTextSwitcher.setText(unescape(mJokes.get(mPosition).getJoke()));
        setFavoriteIcon();
        mProgress.setProgress(mPosition);
    }

    //On category changed / On start

    public void show(){
        mPosition=0;
        mTextSwitcher.setText(unescape(mJokes.get(mPosition).getJoke()));
        setFavoriteIcon();
        mProgress.setProgress(0);
        mProgress.setMax(mJokes.size()-1);

    }

    //This is the method that fixes the \n in textview , thanks stackoverflow https://stackoverflow.com/questions/3586763/new-line-character-n-not-displaying-properly-in-textview-android/6005928#6005928
    private String unescape(String description) {
        return description.replaceAll("\\\\n", "\\\n");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_jokes, menu);
        this.menu = menu;
        show();
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
                    Cursor c = dbcon.getJokes(finalCategories[which]);
                    Log.d("test",finalCategories[which]);
                    parseJokes(c);
                    show();
                    ((MainActivity) getActivity()).setActionBarTitle(finalCategories[which]);


                }
            }).show();
            break;
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, unescape(mJokes.get(mPosition).getJoke()) + " - Shared via El Oh El");
                startActivity(Intent.createChooser(shareIntent, "Share the joke"));
                Answers.getInstance().logShare(new ShareEvent()
                        .putContentId(String.valueOf(mJokes.get(mPosition).getId()))
                        .putContentType(mJokes.get(mPosition).getCategory()));
                break;
            case R.id.favorite:
                if(mJokes.get(mPosition).isFavoriteStatus()) {
                    menu.getItem(2).setIcon(R.drawable.ic_favorite_border_white_48dp);
                    dbcon.unFavorite(mJokes.get(mPosition).getId());
                    mJokes.get(mPosition).setFavoriteStatus(false);
                }else {
                    menu.getItem(2).setIcon(R.drawable.ic_favorite_white_48dp);
                    dbcon.setFavorite(mJokes.get(mPosition).getId());
                    mJokes.get(mPosition).setFavoriteStatus(true);
                }


        }
        return super.onOptionsItemSelected(item);
    }
    public void setFavoriteIcon(){
        if(mJokes.get(mPosition).isFavoriteStatus()) {
            menu.getItem(2).setIcon(R.drawable.ic_favorite_white_48dp);
        }else {
            menu.getItem(2).setIcon(R.drawable.ic_favorite_border_white_48dp);
        }
    }
}
