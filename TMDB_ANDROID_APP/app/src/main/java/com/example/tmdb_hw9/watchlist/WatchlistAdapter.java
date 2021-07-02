package com.example.tmdb_hw9.watchlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tmdb_hw9.R;
import com.example.tmdb_hw9.recyclers.RecyclerData;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.ViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private List<RecyclerData> listItems;
    private Context context;
    private TextView emptyWatchList;

    public WatchlistAdapter(Context context, List<RecyclerData> listItems, TextView emptyWatchList) {
        this.context = context;
        this.listItems = listItems;
        this.emptyWatchList = emptyWatchList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watchlist_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecyclerData listItem = listItems.get(position);

        String media;
        if (listItem.getMedia().equals("tv")) {
            media = "TV";
        } else {
            media = "Movie";
        }

        // Glide is use to load image
        // from url in your imageview.
        Glide.with(holder.itemView)
                .load(listItem.getImgUrl())
                .fitCenter()
                .into(holder.wlPoster);

        holder.wlId.setText(listItem.getId());
        holder.wlMediaType.setText(media);
        holder.wlTitle.setText(listItem.getTitle());
        holder.imgUrl.setText(listItem.getImgUrl());

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, ?> zed = holder.pref.getAll();
                String id = holder.wlId.getText().toString();
                String mediaType = holder.wlMediaType.getText().toString();
                String title = holder.wlTitle.getText().toString();
                holder.editor.remove(id + " " + mediaType.toLowerCase());
                holder.editor.apply();
                zed = holder.pref.getAll();
                Toast.makeText(
                        context.getApplicationContext(),
                        title + " was removed from Watchlist",
                        Toast.LENGTH_SHORT)
                        .show();
                listItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, listItems.size());
                if(listItems.size() == 0) {
                    emptyWatchList.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
//        if (fromPosition < toPosition) {
//            for (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(listItems, i, i + 1);
//            }
//        } else {
//            for (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(listItems, i, i - 1);
//            }
//
//        }

        notifyItemMoved(fromPosition, toPosition);
        Collections.swap(listItems, fromPosition, toPosition);
        //this.listItems = listItems;
    }

    @Override
    public void onRowSelected(ViewHolder myViewHolder) {

    }

    @Override
    public void onRowClear(ViewHolder myViewHolder) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView wlPoster;
        public TextView wlMediaType;
        public TextView wlId;
        public TextView wlTitle;
        public TextView imgUrl;
        public View moveView;
        public CardView cardView;
        public ImageButton imageButton;
        public SharedPreferences pref;
        public SharedPreferences.Editor editor;


        public ViewHolder(View itemView) {
            super(itemView);
            wlPoster = itemView.findViewById(R.id.watchlist_picture);
            wlMediaType = itemView.findViewById(R.id.wl_media_type);
            wlId = itemView.findViewById(R.id.wl_id);
            wlTitle = itemView.findViewById(R.id.wl_title);
            imgUrl = itemView.findViewById(R.id.wl_img);
            cardView = itemView.findViewById(R.id.watchlist_card);
            imageButton = itemView.findViewById(R.id.remove_wl_btn);
            moveView = itemView;

            pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
            editor = pref.edit();


        }

    }
}
