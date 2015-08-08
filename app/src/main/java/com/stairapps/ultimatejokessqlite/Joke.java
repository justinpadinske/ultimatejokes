package com.stairapps.ultimatejokessqlite;

/**
 * Created by tepes on 8/7/2015.
 */
public class Joke {

    private String mText;
    private boolean mFavorited;
    private int mId;
    public Joke(String text, String f,int id){
        mText=text;
        if(f != null)
        mFavorited = true;
        else
            mFavorited = false;
        mId = id;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public boolean ismFavorited() {
        return mFavorited;
    }

    public void setmFavorited(boolean mFavorited) {
        this.mFavorited = mFavorited;
    }

    public int getmId() {
        return mId;
    }
}
