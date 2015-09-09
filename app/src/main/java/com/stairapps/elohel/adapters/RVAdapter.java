package com.stairapps.elohel.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.stairapps.elohel.Joke;
import com.stairapps.elohel.R;
import com.stairapps.elohel.database.SQLController;
import com.stairapps.elohel.fragments.JokesLV;

import java.util.ArrayList;

/**
 * Created by filip on 8/22/15.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.JokesViewHolder>{

    protected ArrayList<Joke> jokes;
    protected SQLController dbcon;

    public RVAdapter(ArrayList<Joke> jokes, SQLController db){
        this.jokes = jokes;
        dbcon = db;
    }

    @Override
    public JokesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
        JokesViewHolder jvh = new JokesViewHolder(v);
        return jvh;
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

        if(jokes.get(position).isFavoriteStatus())
            holder.favButton.setBackgroundResource(R.drawable.ic_favorite_black_48dp);
        else
            holder.favButton.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);

        holder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = jokes.get(position).getId();
                if(dbcon.isFavorited(id)){
                    Log.d("tes","test1");
                    dbcon.unFavorite(id);
                    jokes.get(position).setFavoriteStatus(false);
                    holder.favButton.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                    notifyItemChanged(position);
                }else {
                    Log.d("tes","test2");
                    dbcon.setFavorite(id);
                    jokes.get(position).setFavoriteStatus(true);
                    holder.favButton.setBackgroundResource(R.drawable.ic_favorite_black_48dp);
                    notifyItemChanged(position);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return jokes.size();
    }

    public static class JokesViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView jokeText;
        Button favButton;
        Button shareButton;
        JokesViewHolder(View view){
            super(view);
            cardView = (CardView) view.findViewById(R.id.cv);
            jokeText = (TextView) view.findViewById(R.id.joke);
            favButton = (Button) view.findViewById(R.id.fav);
            shareButton = (Button) view.findViewById(R.id.sharebtn);
        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    //This is used to fix the \n bug
    protected String unescape(String description) {
        return description.replaceAll("\\\\n", "\\\n");
    }
}
