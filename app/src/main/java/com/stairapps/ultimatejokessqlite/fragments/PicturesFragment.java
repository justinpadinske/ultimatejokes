package com.stairapps.ultimatejokessqlite.fragments;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.stairapps.ultimatejokessqlite.MainActivity;
import com.stairapps.ultimatejokessqlite.R;
import com.stairapps.ultimatejokessqlite.pictures.RedditAdapter;
import com.stairapps.ultimatejokessqlite.pictures.RedditController;
import com.stairapps.ultimatejokessqlite.pictures.RedditModel;

public class PicturesFragment extends Fragment {


    private static final String REDDIT_URL = "http://www.reddit.com/user/filipomg/m/elohel.json";
    private ProgressDialog pDialog;
    private List<RedditModel> redditList = new ArrayList<RedditModel>();
    private ListView listView;
    private RedditAdapter adapter;
    private static final String TAG = "PICS";


    public PicturesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pictures, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.list);
        adapter = new RedditAdapter(getActivity(),redditList);
        listView.setAdapter(adapter);

        showProgressDialog();

        Cache cache = RedditController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(REDDIT_URL);

        if(entry!=null){
            //get the data from cache
            try {
                String data = new String (entry.data,"UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else {

            JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET, REDDIT_URL, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    hideProgressDialog();
                    parseJsonFeed(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideProgressDialog();
                    Toast.makeText(getActivity(),"ERROR PARSING",Toast.LENGTH_SHORT).show();
                }
            });

            RedditController.getInstance().addToRequestQueue(jsonReq);

        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).setActionBarTitle("Pictures");

    }

    public void showProgressDialog(){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading");
        pDialog.show();
    }

    public void hideProgressDialog(){
        if(pDialog != null){
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void parseJsonFeed(JSONObject respone){
        try {
            JSONObject redditObj = respone.getJSONObject("data");
            JSONArray redditChildArray = redditObj.getJSONArray("children");
            for(int i = 0; i<redditChildArray.length();i++){
                JSONObject redditChildObj = (JSONObject) redditChildArray.get(i);

                RedditModel redditItem = new RedditModel();
                if(redditChildObj.getJSONObject("data").get("url").toString().contains("imgur")) {
                    redditItem.setTitle(redditChildObj.getJSONObject("data").getString("title"));


                    // Image might be null sometimes
                    String image = redditChildObj.getJSONObject("data").isNull(
                            "thumbnail") ? null : redditChildObj.getJSONObject(
                            "data").getString("thumbnail");

                    redditItem.setThumbnailUrl(image);


                    // Image might be null sometimes
                    String largeImage = redditChildObj.getJSONObject("data")
                            .isNull("preview") ? null : redditChildObj
                            .getJSONObject("data").getJSONObject("preview")
                            .getJSONArray("images").getJSONObject(0)
                            .getJSONObject("source").getString("url");
                    redditItem.setLargeUrl(largeImage);
                    redditList.add(redditItem);
                }
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
