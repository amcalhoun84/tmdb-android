package com.example.tmdb_hw9.recyclers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tmdb_hw9.R;
import com.example.tmdb_hw9.ui.details.DetailsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecRecyclerAdapter extends RecyclerView.Adapter<RecRecyclerAdapter.ViewHolder> {

    private final List<RecyclerData> listItems;
    private final Context context;
    private String id;
    private String mediaType;
    private String title;
    TextView mediaTitle;
    TextView mediaId;

    public RecRecyclerAdapter(Context context, List<RecyclerData> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rec_list_item, parent, false);
        ImageButton imageButton = v.findViewById(R.id.imageButton);
        ImageView imageView = v.findViewById(R.id.imagePoster);
        TextView mediaTitle = v.findViewById(R.id.mediaTitle);
        TextView mediaId = v.findViewById(R.id.mediaId);

        // made it work somehow.
        mediaTitle.setText(title);
        mediaTitle.setVisibility(View.INVISIBLE);
        mediaId.setText(id);
        mediaId.setVisibility(View.INVISIBLE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mediaTitle.getText().toString();
                String id = mediaId.getText().toString();
                Log.i("Film","Hello, you have clicked on " + title);
                Intent detailsActivity = new Intent(context.getApplicationContext(), DetailsActivity.class);
                detailsActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                detailsActivity.putExtra("message", "Moving to another activity.");
                detailsActivity.putExtra("mediaType", mediaType);
                detailsActivity.putExtra("id", id);
                detailsActivity.putExtra("title", title);
                context.startActivity(detailsActivity);

            }
        });

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecRecyclerAdapter.ViewHolder holder, int position) {
        RecyclerData listItem = listItems.get(position);

        // Glide is use to load image
        // from url in your imageview.

        Glide.with(holder.itemView)
                .load(listItem.getImgUrl())
                .fitCenter()
                .into(holder.posterView);



        holder.mediaTitle.setText(listItem.getTitle());
        holder.mediaId.setText(listItem.getId());

        id = listItem.getId();
        mediaType = listItem.getMedia();
        title = listItem.getTitle();

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    private String characterSwap(String s) {
        return s.replaceAll(" ", "-");

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView posterView;
        public ImageButton imageButton;

        public TextView mediaTitle;
        public TextView mediaId;

        public ViewHolder(View itemView) {
            super(itemView);
            posterView = itemView.findViewById(R.id.imagePoster);
            imageButton = itemView.findViewById(R.id.imageButton);
            mediaTitle = itemView.findViewById(R.id.mediaTitle);
            mediaId = itemView.findViewById(R.id.mediaId);
        }

    }

}
