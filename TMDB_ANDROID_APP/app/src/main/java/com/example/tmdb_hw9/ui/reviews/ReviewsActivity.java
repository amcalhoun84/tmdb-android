package com.example.tmdb_hw9.ui.reviews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.tmdb_hw9.R;

public class ReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_TMDB_HW9);
        setContentView(R.layout.activity_reviews);
        Intent intent = getIntent();

        TextView contentActivity = findViewById(R.id.review_content_text);
        TextView ratingsActivity = findViewById(R.id.ratingText2);
        TextView authorActivity = findViewById(R.id.author_text_reviews);

        contentActivity.setText(intent.getStringExtra("content"));
        ratingsActivity.setText(intent.getStringExtra("rating"));
        authorActivity.setText(intent.getStringExtra("author"));
    }
}