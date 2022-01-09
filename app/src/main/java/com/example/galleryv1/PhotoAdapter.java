package com.example.galleryv1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Photo> photos;
    private ArrayList<String> photoSrc = new ArrayList<String>();


    public PhotoAdapter(Context context, ArrayList<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    public PhotoAdapter(Context c) {
        context = c;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public String getImage(int position) {
        return photos.get(position).getSrc();
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_grid_item_photo, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load("file://" + photos.get(position).getThumb())
                .skipMemoryCache(false)
                .into(holder.photoImageView);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, FullImageView.class);

                    Log.d("size",Integer.toString(photos.size()));
//                    Log.d("size2",Integer.toString(photos2.size()));
                    Log.d("position", Integer.toString(position));

                    Bundle photoBundle = new Bundle();
                    photoBundle.putSerializable("photos", photos);
                    intent.putExtra("position", position);
                    intent.putExtra("photoBundle",photoBundle);
                    context.startActivity(intent);
                } catch(Exception e) {
                    int x = 1;
                    Log.e("position",e.getMessage());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return photos == null ? 0 : photos.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView photoImageView;
        CardView mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = (ImageView) itemView.findViewById(R.id.photoImageView);
            mainLayout = (CardView) itemView.findViewById(R.id.mainLayout);
        }
    }
}



