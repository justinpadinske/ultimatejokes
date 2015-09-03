package com.stairapps.elohel.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.stairapps.elohel.MainActivity;
import com.stairapps.elohel.R;
import com.stairapps.elohel.adapters.FVAdapter;
import com.stairapps.elohel.extra.SwipeableRecyclerViewTouchListener;

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
        ((MainActivity) getActivity()).setActionBarTitle("Favorites");;
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
