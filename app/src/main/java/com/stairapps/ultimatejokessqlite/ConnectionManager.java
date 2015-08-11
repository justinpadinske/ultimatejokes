package com.stairapps.ultimatejokessqlite;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by tepes on 8/10/2015.
 */
public class ConnectionManager {

    private static RequestQueue sQueue;


    public static RequestQueue getInstance(Context context){
        if (sQueue == null){
            sQueue = Volley.newRequestQueue(context);
        }
        return sQueue;
    }
    //RequestQueue queue = Volley.newRequestQueue(getActivity());
}
