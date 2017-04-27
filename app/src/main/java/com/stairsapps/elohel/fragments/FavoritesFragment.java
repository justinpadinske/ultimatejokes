package com.stairsapps.elohel.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.analytics.HitBuilders;
import com.stairsapps.elohel.MainActivity;
import com.stairsapps.elohel.R;
import com.stairsapps.elohel.adapters.FVAdapter;
import com.stairsapps.elohel.extra.ApplicationClass;
import com.stairsapps.elohel.extra.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;

/**
 * Created by filip on 9/3/15.
 */
public class FavoritesFragment extends JokesLV{
    public FVAdapter adapter;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.sort);
        item.setVisible(false);
        ((MainActivity) getActivity()).setActionBarTitle("Favorite");;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void showJokes(String category) {
        jokes = new ArrayList<>();
        parseJokes(dbcon.getFavorites());
        adapter=new FVAdapter(jokes,dbcon);//Creating the adapter for the RecylcerView
        rv.setAdapter(adapter);
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(rv,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }


                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    dbcon.unFavorite(jokes.get(position).getId());
                                    jokes.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    dbcon.unFavorite(jokes.get(position).getId());
                                    jokes.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        rv.addOnItemTouchListener(swipeTouchListener);
    }
}
