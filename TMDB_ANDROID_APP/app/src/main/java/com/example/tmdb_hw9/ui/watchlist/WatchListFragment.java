package com.example.tmdb_hw9.ui.watchlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tmdb_hw9.R;
import com.example.tmdb_hw9.recyclers.RecyclerData;
import com.example.tmdb_hw9.watchlist.ItemMoveCallback;
import com.example.tmdb_hw9.watchlist.WatchlistAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class WatchListFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_watch_list, container, false);

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor edit = pref.edit();

        TextView emptyWatchlist = RootView.findViewById(R.id.empty_watchlist);

        ArrayList<RecyclerData> watchListRecyclerData = new ArrayList<>();
        RecyclerView watchListRecycler = RootView.findViewById(R.id.watchListRecycler);
        watchListRecycler.setHasFixedSize(true);
        watchListRecycler.setClipToPadding(true);
        GridLayoutManager gridLayout = new GridLayoutManager(getContext(), 3);
        gridLayout.setOrientation(LinearLayoutManager.VERTICAL);
        watchListRecycler.setLayoutManager(gridLayout);
        watchListRecycler.setNestedScrollingEnabled(false);


        Map<String, ?> zed = pref.getAll(); // because this is the final piece.

        if(zed.size() == 0) {
            emptyWatchlist.setVisibility(View.VISIBLE);
        }

        for (Map.Entry<String, ?> show : zed.entrySet()) {
            try {
                JSONObject json = new JSONObject(show.getValue().toString());
                String media = json.getString("mediaType");
                watchListRecyclerData.add(new RecyclerData(
                        json.getString("id"),
                        json.getString("poster_path"),
                        json.getString("title"),
                        json.getString("mediaType")
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            WatchlistAdapter wlAdapter = new WatchlistAdapter(getContext(),  watchListRecyclerData, emptyWatchlist);
            ItemTouchHelper.Callback callback = new ItemMoveCallback(wlAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(watchListRecycler);

            watchListRecycler.setAdapter(wlAdapter);
        }

        return RootView;
    }

}