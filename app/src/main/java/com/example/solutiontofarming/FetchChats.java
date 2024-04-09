package com.example.solutiontofarming;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.solutiontofarming.data.Extras;

import org.json.JSONArray;

public class FetchChats {

    private static final String TAG = FetchChats.class.getSimpleName();

    private RequestQueue requestQueue;
    private static final String API_URL = Extras.API_URL +"/find/group_chats";

    public FetchChats(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public void fetchData(final FetchNews.ApiResponseListener listener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, API_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        listener.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error);
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    public interface ApiResponseListener {
        void onSuccess(JSONArray response);
        void onError(VolleyError error);
    }
}
