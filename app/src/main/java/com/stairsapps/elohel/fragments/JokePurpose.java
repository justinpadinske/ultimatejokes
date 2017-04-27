package com.stairsapps.elohel.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.stairsapps.elohel.R;
import com.stairsapps.elohel.extra.ApplicationClass;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 */
public class JokePurpose extends Fragment {

    private EditText mName;
    private EditText mEmail;
    private EditText mJoke;
    private Button mSubmit;


    private TextInputLayout tName;
    private TextInputLayout tEmail;
    private TextInputLayout tJoke;

    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");


    //URL derived from form URL
    public static final String URL="https://docs.google.com/forms/d/1VBATgib4-2Ql9qEz37JxMPMyMfpaoby1ziW7kdJehyg/formResponse";

    //input element ids found from the live form page
    public static final String NAME_KEY="entry.841618648";
    public static final String EMAIL_KEY="entry.207141387";
    public static final String JOKE_KEY="entry.1012434953";

    private Context context;
    public JokePurpose() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();



        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Make sure all the fields are filled with values
                if (TextUtils.isEmpty(mEmail.getText().toString()) ||
                        TextUtils.isEmpty(mName.getText().toString()) ||
                        TextUtils.isEmpty(mJoke.getText().toString())) {
                    Toast.makeText(context, "Toate campurile sunt obligatorii", Toast.LENGTH_LONG).show();
                    return;
                }
                //Check if a valid email is entered
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches()) {
                    tEmail.setErrorEnabled(true);
                    tEmail.setError("Please enter a valid email address");
                    return;
                }

                //Create an object for PostDataTask AsyncTask
                PostDataTask postDataTask = new PostDataTask();

                postDataTask.execute(URL, mName.getText().toString(),
                        mEmail.getText().toString(),
                        mJoke.getText().toString());


            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_joke_purpose, container, false);
        mName = (EditText) v.findViewById(R.id.name);
        mEmail = (EditText) v.findViewById(R.id.email);
        mJoke = (EditText) v.findViewById(R.id.joke);
        mSubmit = (Button) v.findViewById(R.id.submit_btn);
        tName = (TextInputLayout) v.findViewById(R.id.til);
        tEmail = (TextInputLayout) v.findViewById(R.id.ti2);
        tJoke = (TextInputLayout) v.findViewById(R.id.til3);

        return v;
    }


    //AsyncTask to send data as a http POST request
    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            String url = contactData[0];
            String name = contactData[1];
            String email = contactData[2];
            String joke = contactData[3];
            String postBody = "";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody = EMAIL_KEY + "=" + URLEncoder.encode(email, "UTF-8") +
                        "&" + NAME_KEY + "=" + URLEncoder.encode(name, "UTF-8") +
                        "&" + JOKE_KEY + "=" + URLEncoder.encode(joke, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                result = false;
            }


            try {
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();
            } catch (IOException exception) {
                result = false;
            }
            return result;
        }
        @Override
        protected void onPostExecute(Boolean result){
            //Print Success or failure message accordingly
            Toast.makeText(context,result?"Gluma a fost trimisa cu succes!":"Ooops! A aparut o eroare!",Toast.LENGTH_LONG).show();
        }
    }
}
