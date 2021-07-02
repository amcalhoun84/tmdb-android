package com.example.tmdb_hw9.ui.home.tabs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tmdb_hw9.R;
import com.example.tmdb_hw9.recyclers.RecyclerAdapter;
import com.example.tmdb_hw9.recyclers.RecyclerData;
import com.example.tmdb_hw9.sliders.SliderAdapter;
import com.example.tmdb_hw9.sliders.SliderData;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TVFragment extends Fragment {

    public TVFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_tv, container, false);
        //Spinner spinner = (Spinner) RootView.findViewById(R.id.spinner); // we'll worry about this once we have major functionality done

        SliderView trendingSlider = RootView.findViewById(R.id.tv_trending_now_slider);
        RecyclerView trRecyclerView = (RecyclerView) RootView.findViewById(R.id.top_rated_tv_recyclerView);
        trRecyclerView.setHasFixedSize(true);
        trRecyclerView.setClipToPadding(true);
        trRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        trRecyclerView.setNestedScrollingEnabled(false);

        RecyclerView popRecyclerView = (RecyclerView) RootView.findViewById(R.id.popular_tv_recyclerView);
        popRecyclerView.setHasFixedSize(true);
        popRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        ArrayList<SliderData> trendingSliderDataArrayList = new ArrayList<>();
        ArrayList<RecyclerData> trRecyclerDataArrayList = new ArrayList<>();
        ArrayList<RecyclerData> popRecyclerDataArrayList = new ArrayList<>();

        TextView footer = (TextView) RootView.findViewById(R.id.footer);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        String trending = "https://end-of-game-day.wl.r.appspot.com/api/trending/tv/day";
        String topRated = "https://end-of-game-day.wl.r.appspot.com/api/category/tv/top_rated";
        String popular = "https://end-of-game-day.wl.r.appspot.com/api/category/tv/popular";

        StringRequest trendingStringRequest = new StringRequest(Request.Method.GET, trending,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Debug", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                trendingSliderDataArrayList.add(new SliderData(
                                        jo.getString("id"),
                                        jo.getString("poster_path"),
                                        jo.getString("title"),
                                        "tv"));
                            }
                            SliderAdapter trendingAdapter = new SliderAdapter(getContext(), trendingSliderDataArrayList);
                            trendingSlider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                            trendingSlider.setSliderAdapter(trendingAdapter);
                            trendingSlider.setScrollTimeInMillis(1000);
                            trendingSlider.setAutoCycle(true);
                            trendingSlider.startAutoCycle();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });

        StringRequest trStringRequest = new StringRequest(Request.Method.GET, topRated,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string;
                        Log.i("Debug", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                trRecyclerDataArrayList.add(new RecyclerData(
                                        jo.getString("id"),
                                        jo.getString("poster_path"),
                                        jo.getString("title"),
                                        "tv"));
                            }
                            RecyclerAdapter trAdapter = new RecyclerAdapter(getContext(), trRecyclerDataArrayList);
                            trRecyclerView.setAdapter(trAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });

        StringRequest popStringRequest = new StringRequest(Request.Method.GET, popular,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string;
                        Log.i("Debug", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                popRecyclerDataArrayList.add(new RecyclerData(
                                        jo.getString("id"),
                                        jo.getString("poster_path"),
                                        jo.getString("title"),
                                        "tv"));
                            }
                            RecyclerAdapter popAdapter = new RecyclerAdapter(getContext(), popRecyclerDataArrayList);
                            popRecyclerView.setAdapter(popAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(trendingStringRequest);
        queue.add(trStringRequest);
        queue.add(popStringRequest);

        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TMDBIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/"));
                startActivity(TMDBIntent);
            }
        });


        return RootView;
    }
}
