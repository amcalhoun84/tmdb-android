package com.example.tmdb_hw9.ui.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tmdb_hw9.R;
import com.example.tmdb_hw9.search.recyclers.SearchAdapter;
import com.example.tmdb_hw9.search.recyclers.SearchData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_search, container, false);
        TextView noResults = (TextView) RootView.findViewById(R.id.no_search_results);
        noResults.setVisibility(View.INVISIBLE);
        SearchView searchView = (SearchView) RootView.findViewById(R.id.searchView);
        RecyclerView searchRecyclerView = (RecyclerView) RootView.findViewById(R.id.searchRecycler);
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String nowPlaying = "https://end-of-game-day.wl.r.appspot.com/api/multi/";

        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //txtHeader.setVisibility(View.GONE);
                searchView.clearFocus();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<SearchData> searchRecyclerDataArrayList = new ArrayList<>();
                if (s.equals("")) {
                    SearchAdapter searchAdapter = new SearchAdapter(getContext(), searchRecyclerDataArrayList);
                    searchRecyclerView.setAdapter(searchAdapter);
                    noResults.setVisibility(View.INVISIBLE);
                    return false;
                }
                String url = nowPlaying;
                url += s;
                Log.i("Info", url);
                StringRequest searchStringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    if(jsonArray.length() == 0) {
                                        noResults.setVisibility(View.VISIBLE);
                                    } else {
                                        noResults.setVisibility(View.INVISIBLE);
                                    }
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jo = jsonArray.getJSONObject(i);
                                        searchRecyclerDataArrayList.add(new SearchData(
                                                jo.getString("id"),
                                                jo.getString("poster_path"),
                                                jo.getString("name"),
                                                jo.getString("rating"),
                                                jo.getString("release_date"),
                                                jo.getString("media_type")
                                        ));
                                    }
                                    SearchAdapter searchAdapter = new SearchAdapter(getContext(), searchRecyclerDataArrayList);
                                    searchRecyclerView.setAdapter(searchAdapter);
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
                queue.add(searchStringRequest);
                return false;
            }
        });


        return RootView;
    }


}