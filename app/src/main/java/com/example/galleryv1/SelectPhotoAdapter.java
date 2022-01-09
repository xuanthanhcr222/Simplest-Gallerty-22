package com.example.galleryv1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class SelectPhotoAdapter extends RecyclerView.Adapter<SelectPhotoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Photo> photos;
    private ArrayList<String> addedPhotoSrc = new ArrayList<String>();

//    private Boolean isSelectMenu = false;


    public SelectPhotoAdapter(Context context, ArrayList<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    public SelectPhotoAdapter(Context c) {
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

    public ArrayList<String> getAddedPhotoSrc() {
        return addedPhotoSrc;
    }

    public void setAddedPhotoSrc(ArrayList<String> addedPhotoSrc) {
        this.addedPhotoSrc = addedPhotoSrc;
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

                if(holder.photoImageView.getImageAlpha() == 255){
                    holder.photoImageView.setImageAlpha(50);
//                Toast.makeText(getContext(), "Select Photo!", Toast.LENGTH_SHORT).show();
                    String src = photos.get(position).getSrc();
                    addedPhotoSrc.add(src);
                }else{
                    holder.photoImageView.setImageAlpha(255);
                    String src = photos.get(position).getSrc();
                    addedPhotoSrc.remove(src);
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



