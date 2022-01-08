package com.example.galleryv1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {


    Context context;
    ArrayList<Video>arr;
    Activity activity;

    public VideoAdapter(Context context, ArrayList<Video> arr, Activity activity) {
        this.context = context;
        this.arr = arr;
        this.activity=activity;
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_video, parent, false);
        return new VideoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load("file://"+arr.get(position).getThumb())
                .skipMemoryCache(false)
                .into(holder.photoImageView);
        holder.mainLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        holder.mainLayout.setAlpha(0);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,FullVideoView.class);
                intent.putExtra("video",arr.get(position).getPath());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView photoImageView;
        RelativeLayout mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = (ImageView) itemView.findViewById(R.id.vd_image);
            mainLayout = (RelativeLayout) itemView.findViewById(R.id.r1_select);
        }
    }
}

