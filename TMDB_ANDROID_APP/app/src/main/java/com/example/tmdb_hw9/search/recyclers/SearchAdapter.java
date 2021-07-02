package com.example.tmdb_hw9.search.recyclers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tmdb_hw9.R;
import com.example.tmdb_hw9.ui.details.DetailsActivity;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<SearchData> listItems;
    private Context context;
    private String id;
    private String media_type;
    private String title;

    public SearchAdapter(Context context, List<SearchData> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);

        ImageView imageView = v.findViewById(R.id.imageSearch);
        TextView mediaTitle = v.findViewById(R.id.cardTitle);
        TextView mediaId = v.findViewById(R.id.cardId);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mediaTitle.getText().toString();
                String id = mediaId.getText().toString();
                Log.i("Film","Hello, you have clicked on " + title);
                Intent detailsActivity = new Intent(context, DetailsActivity.class);
                detailsActivity.putExtra("message", "Moving to another activity.");
                detailsActivity.putExtra("mediaType", media_type);
                detailsActivity.putExtra("id", id);
                detailsActivity.putExtra("title", title);
                context.startActivity(detailsActivity);

            }
        });

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchData listItem = listItems.get(position);

        // Glide is use to load image
        // from url in your imageview.
        Glide.with(holder.itemView)
                .load(listItem.getImgUrl())
                .fitCenter()
                .into(holder.cardImage);

        String cardMY = listItem.getMediaType().toUpperCase() + " (" + listItem.getReleaseDate() + ")";

        holder.cardTitle.setText(listItem.getTitle());
        holder.cardMediaYear.setText(cardMY);
        holder.cardRating.setText(listItem.getRating());
        holder.cardId.setText(listItem.getId());

        media_type = listItem.getMediaType();
        id = listItem.getId();
        title = listItem.getTitle();

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView cardImage;
        public TextView cardTitle;
        public TextView cardMediaYear;
        public TextView cardRating;
        public TextView cardId;


        public ViewHolder(View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.imageSearch);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardMediaYear = itemView.findViewById(R.id.cardMediaYear);
            cardRating = itemView.findViewById(R.id.cardRating);
            cardId = itemView.findViewById(R.id.cardId);
        }

    }
}
