package com.example.tmdb_hw9.sliders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.example.tmdb_hw9.R;
import com.example.tmdb_hw9.ui.details.DetailsActivity;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    private final List<SliderData> mSliderItems;
    private final Context context;
    private String id;
    private String mediaType;
    private String title;
    TextView mediaTitle;
    TextView mediaId;
    TextView media;

    // Constructor
    public SliderAdapter(Context context, ArrayList<SliderData> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;
        this.context = context;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        TextView mediaTitle = (TextView) inflate.findViewById(R.id.sliderTitle);
        TextView mediaId = (TextView) inflate.findViewById(R.id.sliderId);
        TextView media = (TextView) inflate.findViewById(R.id.sliderMedia);

        mediaTitle.setVisibility(View.INVISIBLE);
        mediaId.setVisibility(View.INVISIBLE);
        media.setVisibility(View.INVISIBLE);

        ImageView imagePoster = (ImageView) inflate.findViewById(R.id.myimage);
        ImageView imageBlur = (ImageView) inflate.findViewById(R.id.myimageblur);

        imagePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mediaTitle.getText().toString();
                String id = mediaId.getText().toString();
                String mediaType = media.getText().toString();
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

        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, int position) {

        final SliderData sliderItem = mSliderItems.get(position);

        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImgUrl())
                .fitCenter()
                .transform(new jp.wasabeef.glide.transformations.BlurTransformation(10, 2))
                .into(viewHolder.imageViewBackgroundBlur);

        // Glide is use to load image
        // from url in your imageview.
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImgUrl())
                .fitCenter()
                .into(viewHolder.imageViewBackground);

        id = sliderItem.getId();
        title = sliderItem.getTitle();
        mediaType = sliderItem.getMedia();

        viewHolder.titleSlider.setText(title);
        viewHolder.mediaSlider.setText(mediaType);
        viewHolder.idSlider.setText(id);


    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView imageViewBackground;
        ImageView imageViewBackgroundBlur;
        TextView titleSlider;
        TextView mediaSlider;
        TextView idSlider;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            imageViewBackgroundBlur = itemView.findViewById(R.id.myimageblur);
            titleSlider = itemView.findViewById(R.id.sliderTitle);
            mediaSlider = itemView.findViewById(R.id.sliderMedia);
            idSlider = itemView.findViewById(R.id.sliderId);

            this.itemView = itemView;
        }
    }
}
