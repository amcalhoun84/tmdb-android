package com.example.tmdb_hw9.recyclers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.ContextThemeWrapper;
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
import com.example.tmdb_hw9.MainActivity;
import com.example.tmdb_hw9.R;
import com.example.tmdb_hw9.ui.details.DetailsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final List<RecyclerData> listItems;
    private final Context context;
    private String id;
    private String mediaType;
    private String title;
    private String imgUrl;
    TextView mediaTitle;
    TextView mediaId;
    boolean inWatchlist = false;


    public RecyclerAdapter(Context context, List<RecyclerData> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        ImageButton imageButton = v.findViewById(R.id.imageButton);
        ImageView imageView = v.findViewById(R.id.imagePoster);
        TextView mediaTitle = v.findViewById(R.id.mediaTitle);
        TextView mediaId = v.findViewById(R.id.mediaId);
        TextView mediaImg = v.findViewById(R.id.mediaImg);

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
                String imgUrl = mediaImg.getText().toString();
                Log.i("Film", "Hello, you have clicked on " + title);
                Intent detailsActivity = new Intent(context.getApplicationContext(), DetailsActivity.class);
                detailsActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                detailsActivity.putExtra("message", "Moving to another activity.");
                detailsActivity.putExtra("mediaType", mediaType);
                detailsActivity.putExtra("id", id);
                detailsActivity.putExtra("poster_path", imgUrl);
                detailsActivity.putExtra("title", title);
                context.startActivity(detailsActivity);

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mediaTitle.getText().toString();
                String id = mediaId.getText().toString();
                String imgUrl = mediaImg.getText().toString();
                SharedPreferences pref = parent.getContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                Context wrapper = new ContextThemeWrapper(context, R.style.popupMenuStyle);
                PopupMenu popupMenu = new PopupMenu(wrapper, imageButton);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                if(pref.getString(id + " " + mediaType, null) != null) {
                    inWatchlist = true;
                    popupMenu.getMenu().findItem(R.id.addWatchlist).setTitle(R.string.remove_from_watchlist);
                }


                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch ((String) item.getTitle()) {
                            case "Open in TMDB":
                                Intent TMDBIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/" + mediaType + "/" + id + "-" + characterSwap(title.toLowerCase()) + "?language=en-US"));
                                context.startActivity(TMDBIntent);
                                break;

                            case "Share on Twitter":
                                Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=Come watch " + title + " with me! https://www.themoviedb.org/" + mediaType + "/" + id + "-" + characterSwap(title.toLowerCase()) + "?language=en-US"));
                                context.startActivity(twitterIntent);
                                break;

                            case "Share on Facebook":
                                Intent fbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u=https://www.themoviedb.org/" + mediaType + "/" + id + "-" + characterSwap(title.toLowerCase()) + "?language=en-US"));
                                context.startActivity(fbIntent);
                                break;

                            case "Add to Watchlist":
                                editor.putString(id + " " + mediaType, "{\"title\": \"" + title + "\", \"mediaType\": \"" + mediaType + "\", \"id\": \"" + id + "\", \"poster_path\": \"" + imgUrl + "\" }");
                                editor.apply();

                                Toast.makeText(
                                        parent.getContext(),
                                        title + " was added to Watchlist",
                                        Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            case "Remove from Watchlist":
                                editor.remove(id + " " + mediaType);
                                editor.apply();
                                Toast.makeText(
                                        parent.getContext(),
                                        title + " was removed from Watchlist",
                                        Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            default:
                                Toast.makeText(
                                        parent.getContext(),
                                        "You clicked " + item.getTitle(),
                                        Toast.LENGTH_LONG)
                                        .show();
                                break;

                        }
                        return true;
                    }
                });
                popupMenu.show();

            }
        });

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        RecyclerData listItem = listItems.get(position);

        // Glide is use to load image
        // from url in your imageview.

        Glide.with(holder.itemView)
                .load(listItem.getImgUrl())
                .fitCenter()
                .into(holder.posterView);


        holder.mediaTitle.setText(listItem.getTitle());
        holder.mediaId.setText(listItem.getId());
        holder.mediaImg.setText(listItem.getImgUrl());

        id = listItem.getId();
        mediaType = listItem.getMedia();
        title = listItem.getTitle();
        imgUrl = listItem.getImgUrl();


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
        public TextView mediaImg;

        public ViewHolder(View itemView) {
            super(itemView);
            posterView = itemView.findViewById(R.id.imagePoster);
            imageButton = itemView.findViewById(R.id.imageButton);
            mediaTitle = itemView.findViewById(R.id.mediaTitle);
            mediaId = itemView.findViewById(R.id.mediaId);
            mediaImg = itemView.findViewById(R.id.mediaImg);
        }

    }

}
