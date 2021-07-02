package com.example.tmdb_hw9.cast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tmdb_hw9.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    private List<CastData> listItems;
    private Context context;

    public CastAdapter(Context context, List<CastData> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cast_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CastData listItem = listItems.get(position);

        // Glide is use to load image
        // from url in your imageview.
        Glide.with(holder.itemView)
                .load(listItem.getImgUrl())
                .fitCenter()
                .into(holder.actorPic);

        holder.actorName.setText(listItem.getName());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView actorPic;
        public TextView actorName; // may not be necessary.

        public ViewHolder(View itemView) {
            super(itemView);
            actorPic = itemView.findViewById(R.id.cast_photo);
            actorName = itemView.findViewById(R.id.actor_name);
        }

    }
}
