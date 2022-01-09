package com.example.galleryv1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Album> albums;


    public AlbumAdapter(Context context, ArrayList<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    public AlbumAdapter(Context c) {
        context = c;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public String getThumbnail(int position) {
        return albums.get(position).getThumbnail();
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_grid_item_album, parent, false);
        return new AlbumAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

//        Glide.with(context).load("file://"+albums.get(position).getThumbnail())
//                .skipMemoryCache(false)
//                .into(holder.albumImageView);

        Glide.with(context).load(albums.get(position).getThumbnail())
                .skipMemoryCache(false)
                .into(holder.albumImageView);

//        String thumbnail = getThumbnail(position);
////        holder.albumImageView.setImageResource(thumbnail);
//
//        File file = new File(thumbnail);
//        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//        holder.albumImageView.setImageBitmap(myBitmap);


        holder.albumName.setText(getAlbums().get(position).getName());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AlbumDetail.class);

                Album album = albums.get(position);
                Bundle albumBundle = new Bundle();
                albumBundle.putSerializable("album", album);
                intent.putExtra("albumBundle",albumBundle);
                context.startActivity(intent);
            }
        });
    }

    // public String getDemension(int s){
    //     BitmapFactory.Options o = new BitmapFactory.Options();
    //     o.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
    //     Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), s, o);
    //     int w = bmp.getWidth();
    //     int h = bmp.getHeight();
    //     String result = w + " x " + h;
    //     return result;
    // }


    @Override
    public int getItemCount() {
        return albums == null ? 0 : albums.size();
    }


     public static class ViewHolder extends RecyclerView.ViewHolder {

         ImageView albumImageView;
         TextView albumName;
         CardView mainLayout;

         public ViewHolder(@NonNull View itemView) {
             super(itemView);
             albumImageView = (ImageView) itemView.findViewById(R.id.albumThumpImageView);
             mainLayout = (CardView) itemView.findViewById(R.id.mainLayout);
             albumName = (TextView) itemView.findViewById(R.id.albumName);


         }
     }
}
