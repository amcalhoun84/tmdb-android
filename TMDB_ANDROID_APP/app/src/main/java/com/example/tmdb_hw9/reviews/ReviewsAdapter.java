package com.example.tmdb_hw9.reviews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tmdb_hw9.R;
import com.example.tmdb_hw9.ui.reviews.ReviewsActivity;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<ReviewsData> listItems;
    private Context context;
    private String id;
    private String media_type;
    private String title;

    public ReviewsAdapter(Context context, List<ReviewsData> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        CardView cardView = v.findViewById(R.id.review_card);

        TextView contentView = v.findViewById(R.id.reviewText);
        TextView ratingtView = v.findViewById(R.id.ratingText);
        TextView authorView = v.findViewById(R.id.authorDateText);


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentView.getText().toString();
                String rating = ratingtView.getText().toString();
                String author = authorView.getText().toString();

                Intent reviewsActivity = new Intent(context.getApplicationContext(), ReviewsActivity.class);
                reviewsActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                reviewsActivity.putExtra("message", "Moving to another activity.");
                reviewsActivity.putExtra("content", content);
                reviewsActivity.putExtra("author", author);
                reviewsActivity.putExtra("rating", rating);
                context.startActivity(reviewsActivity);
            }
        });

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReviewsData listItem = listItems.get(position);


        String date = listItem.getCreatedDate();
        date = date.substring(0, 3) + ", " + date.substring(4, 15);


        String s = "by " + listItem.getUsername() + " on " + date;
        String r = listItem.getRating() + " / 5";

        holder.reviewRatingText.setText(r);
        holder.reviewText.setText(listItem.getContent());
        holder.userNameText.setText(s);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView reviewRatingText;
        public TextView reviewText;
        public TextView userNameText;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewRatingText = (TextView) itemView.findViewById(R.id.ratingText);
            reviewText = (TextView) itemView.findViewById(R.id.reviewText);
            // YOU NEED TO MAKE THESE.
            userNameText = (TextView) itemView.findViewById(R.id.authorDateText);
            //createdDateText = (TextView) itemView.findViewById(R.id.createdDateText);
        }

    }
}
