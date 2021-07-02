package com.example.tmdb_hw9.ui.details;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.example.tmdb_hw9.R;
import com.example.tmdb_hw9.cast.CastAdapter;
import com.example.tmdb_hw9.cast.CastData;
import com.example.tmdb_hw9.recyclers.RecRecyclerAdapter;
import com.example.tmdb_hw9.reviews.ReviewsAdapter;
import com.example.tmdb_hw9.recyclers.RecyclerAdapter;
import com.example.tmdb_hw9.recyclers.RecyclerData;
import com.example.tmdb_hw9.reviews.ReviewsData;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private String title;
    private String id;
    private String mediaType;
    private String imgUrl;
    private String overviewContent;
    private String genres;
    private String releaseDate;
    private String backdropPath;
    private ProgressBar spinner;
    private TextView loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_TMDB_HW9);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();

        TextView mediaTitle = findViewById(R.id.titleFilmText);
        ReadMoreTextView overviewText = findViewById(R.id.overviewText);
        TextView releaseValue = findViewById(R.id.releaseValue);
        TextView genreValue = findViewById(R.id.genreValue);
        TextView reviewsTitle = findViewById(R.id.reviewsTitle);
        TextView castTitle = findViewById(R.id.castTitle);
        TextView recTitle = findViewById(R.id.recPicksText);
        spinner = findViewById(R.id.detailsProgressBar);
        loading = findViewById(R.id.detailsLoading);


        ImageView backdropPoster = findViewById(R.id.backdropImage);

        YouTubePlayerView youTubePlayer = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayer);

        ImageView addminus = findViewById(R.id.watchlist_button);
        ImageView faceBookBtn = findViewById(R.id.fb_button);
        ImageView twitterBtn = findViewById(R.id.twitter_button);

        title = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        mediaType = intent.getStringExtra("mediaType");
        imgUrl = intent.getStringExtra("poster_path");


        mediaTitle.setText(title);

        RequestQueue queue = Volley.newRequestQueue(this);

        RecyclerView castRecycler = (RecyclerView) findViewById(R.id.castPictureRecyclers);
        GridLayoutManager gridLayout = new GridLayoutManager(this, 3);
        gridLayout.setOrientation(LinearLayoutManager.VERTICAL);
        castRecycler.setLayoutManager(gridLayout);;
        castRecycler.setNestedScrollingEnabled(false);

        RecyclerView reviewsRecycler = (RecyclerView) findViewById(R.id.reviewsRecycler);
        reviewsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        RecyclerView recRecyclerView = (RecyclerView) findViewById(R.id.recommendedRecycler);
        recRecyclerView.setHasFixedSize(true);
        recRecyclerView.setClipToPadding(true);
        recRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recRecyclerView.setNestedScrollingEnabled(false);

        ArrayList<ReviewsData> reviewsDataList = new ArrayList<>();
        ArrayList<CastData> castDataList = new ArrayList<>();
        ArrayList<RecyclerData> recommendedPicksList = new ArrayList<>();


        String castURL = "https://end-of-game-day.wl.r.appspot.com/api/cast/" + mediaType + "/" + id;
        String reviewsURL = "https://end-of-game-day.wl.r.appspot.com/api/reviews/" + mediaType + "/" + id;
        String watchURL = "https://end-of-game-day.wl.r.appspot.com/api/watch/" + mediaType + "/" + id;
        String detailsURL = "https://end-of-game-day.wl.r.appspot.com/api/details/" + mediaType + "/" + id;
        String recommendationsURL = "https://end-of-game-day.wl.r.appspot.com/api/recommendations/" + mediaType + "/" + id + "/recommendations";

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();

        if(pref.getString(id + " " + mediaType, null) != null) {
            addminus.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
        }

        StringRequest details = new StringRequest(Request.Method.GET, detailsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string;
                        try {
                            JSONObject json = new JSONObject(response);
                            overviewContent = json.getString("overview");
                            releaseDate = json.getString("release_date");
                            genres = processArray(json.getString("genres"));
                            backdropPath = json.getString("backdrop_path");
                            imgUrl = json.getString("poster_path");
                            overviewText.setText(overviewContent);
                            genreValue.setText(genres);
                            releaseValue.setText(releaseDate);

                            Glide.with(backdropPoster)
                                    .load(backdropPath)
                                    .into(backdropPoster);

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

        StringRequest watch = new StringRequest(Request.Method.GET, watchURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string;
                        try {
                            JSONArray json = new JSONArray(response);
                            if(json.length() == 0) {
                                youTubePlayer.setVisibility(View.INVISIBLE);
                                backdropPoster.setVisibility(View.VISIBLE);
                            }
                            for (int i = 0; i < json.length(); i++) {
                                JSONObject jo = json.getJSONObject(i);
                                if (jo.getString("type").equals("Trailer") || jo.getString("type").equals("Teaser")) {
                                    youTubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                        @Override
                                        public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                                            try {
                                                youTubePlayer.loadVideo(jo.getString("key"), 0);
                                                youTubePlayer.pause();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    break;
                                }
                            }
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });

        StringRequest cast = new StringRequest(Request.Method.GET, castURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string;
                        try {
                            JSONArray json = new JSONArray(response);
                            for (int i = 0; i < json.length(); i++) {
                                JSONObject jo = json.getJSONObject(i);
                                castDataList.add(new CastData(
                                        jo.getString("name"),
                                        jo.getString("profile_path")
                                ));
                            }
                            CastAdapter castAdapter = new CastAdapter(getBaseContext(), castDataList);
                            castRecycler.setAdapter(castAdapter); //not implemented yet.
                            if (json.length() >= 4) {
                                LinearLayout cast_layout = findViewById(R.id.cast_layout);
                                ViewGroup.LayoutParams params = cast_layout.getLayoutParams();
                                params.height = 1200;
                                cast_layout.setLayoutParams(params);
                            }

                            if (json.length() <= 3 && json.length() > 0) {
                                LinearLayout cast_layout = findViewById(R.id.cast_layout);
                                ViewGroup.LayoutParams params = cast_layout.getLayoutParams();
                                params.height = 600;
                                cast_layout.setLayoutParams(params);
                            }

                            if (json.length() == 0) {
                                castTitle.setVisibility(View.GONE);
                                castRecycler.setVisibility(View.GONE);
                            }

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

        StringRequest reviews = new StringRequest(Request.Method.GET, reviewsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string;
                        try {
                            JSONArray json = new JSONArray(response);
                            for (int i = 0; i < json.length(); i++) {
                                JSONObject jo = json.getJSONObject(i);
                                reviewsDataList.add(new ReviewsData(
                                        jo.getString("rating"),
                                        jo.getString("author"),
                                        jo.getString("content"),
                                        jo.getString("created_date"))
                                );
                            }
                            ReviewsAdapter reviewAdapter = new ReviewsAdapter(getBaseContext(), reviewsDataList);
                            reviewsRecycler.setAdapter(reviewAdapter);

                            if (json.length() == 0) {
                                reviewsTitle.setVisibility(View.GONE);
                                reviewsRecycler.setVisibility(View.GONE);
                            }

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

        StringRequest recs = new StringRequest(Request.Method.GET, recommendationsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                recommendedPicksList.add(new RecyclerData(
                                        jo.getString("id"),
                                        jo.getString("poster_path"),
                                        jo.getString("title"),
                                        "movie"));
                            }
                            RecRecyclerAdapter recAdapter = new RecRecyclerAdapter(getBaseContext(), recommendedPicksList);
                            recRecyclerView.setAdapter(recAdapter);

                            if(jsonArray.length() == 0) {
                                recTitle.setVisibility(View.GONE);
                                recRecyclerView.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        spinner.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });

        queue.add(details);
        queue.add(reviews);
        queue.add(cast);
        queue.add(watch);
        queue.add(recs);

        addminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getString(id + " " + mediaType, null) == null) {
                    editor.putString(id + " " + mediaType, "{\"title\": \"" + title + "\", \"mediaType\": \"" + mediaType + "\", \"id\": \"" + id + "\", \"poster_path\": \"" + imgUrl + "\" }");
                    editor.apply();
                    addminus.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);


                    Toast.makeText(
                            getApplicationContext(),
                            title + " was added to Watchlist",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    editor.remove(id + " " + mediaType);
                    editor.apply();
                    addminus.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
                    Toast.makeText(
                            getApplicationContext(),
                            title + " was removed from Watchlist",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=Come watch " + title + " with me! https://www.themoviedb.org/" + mediaType + "/" + id + "-" + characterSwap(title.toLowerCase()) + "?language=en-US"));
                startActivity(twitterIntent);
            }
        });

        faceBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u=https://www.themoviedb.org/" + mediaType + "/" + id + "-" + characterSwap(title.toLowerCase()) + "?language=en-US"));
                startActivity(fbIntent);
            }
        });
    }

    private String processArray(String array) {
        // It's an "array", we're just gonna trim some stuff...
        array = array.replaceAll("[\\[\\]\"]", "").trim();
        return array;
    }

    private String characterSwap(String s) {
        return s.replaceAll(" ", "-");

    }
}