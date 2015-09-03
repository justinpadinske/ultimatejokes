package com.stairapps.elohel.adapters;

import android.content.Intent;
import android.view.View;

import com.stairapps.elohel.Joke;
import com.stairapps.elohel.R;
import com.stairapps.elohel.adapters.RVAdapter;
import com.stairapps.elohel.database.SQLController;

import java.util.ArrayList;

/**
 * Created by filip on 9/3/15.
 */
public class FVAdapter extends RVAdapter{


    public FVAdapter(ArrayList<Joke> jokes, SQLController db) {
        super(jokes, db);
    }

    @Override
    public void onBindViewHolder(final JokesViewHolder holder, final int position) {
        holder.jokeText.setText(unescape(jokes.get(position).getJoke()));
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, holder.jokeText.getText().toString() + " - Shared via El Oh El");
                v.getContext().startActivity(shareIntent);
            }
        });

        if((jokes.get(position).isFavoriteStatus()))
            holder.favButton.setBackgroundResource(R.drawable.ic_favorite_black_48dp);

        holder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = jokes.get(position).getId();
                if (dbcon.isFavorited(id)) {
                    dbcon.unFavorite(id);
                    jokes.remove(position);
                    notifyItemRemoved(position);
                    holder.favButton.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                }
                notifyDataSetChanged();
            }
        });
    }
}
