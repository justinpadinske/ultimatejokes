package com.stairsapps.elohel;

/**
 * Created by tepes on 8/7/2015.
 */
public class Joke {

    int id;
    String joke;
    boolean favoriteStatus;
    String category;


    public Joke(){

    }

    public Joke(int id, String joke, boolean favoriteStatus, String category) {
        this.id = id;
        this.joke = joke;
        this.favoriteStatus = favoriteStatus;
        this.category = category;
    }

    //Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public boolean isFavoriteStatus() {
        return favoriteStatus;
    }

    public void setFavoriteStatus(boolean favoriteStatus) {
        this.favoriteStatus = favoriteStatus;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}