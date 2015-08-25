package com.stairapps.ultimatejokessqlite;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by filip on 8/22/15.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.JokesViewHolder>{

    ArrayList<Joke> jokes;
    public RVAdapter(ArrayList<Joke> jokes){
        this.jokes = jokes;
    }

    @Override
    public JokesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
        JokesViewHolder jvh = new JokesViewHolder(v);
        return jvh;
    }

    @Override
    public void onBindViewHolder(JokesViewHolder holder, int position) {

        holder.jokeText.setText(unescape(jokes.get(position).getmText()));

    }

    @Override
    public int getItemCount() {
        return jokes.size();
    }

    public static class JokesViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView jokeText;

        JokesViewHolder(View view){
            super(view);
            cardView = (CardView) view.findViewById(R.id.cv);
            jokeText = (TextView) view.findViewById(R.id.joke);

        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    //This is used to fix the \n bug
    private String unescape(String description) {
        return description.replaceAll("\\\\n", "\\\n");
    }
}
