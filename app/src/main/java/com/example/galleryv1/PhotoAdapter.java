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

        //for (int i = 0; i < photos.size(); i++) {
        //    String src = photos.get(i).getSrc();
        //    photoSrc.add(src);
        //}

//        String photoSrc = photos.get(position).getThumb();
//        holder.photoImageView.setImageResource(photoSrc);

//        File file = new File(photoSrc);
//        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//        holder.photoImageView.setImageBitmap(myBitmap);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(context, FullImageView.class);
                //Photo photo = photos.get(position);
                //Bundle photoBundle = new Bundle();
                //photoBundle.putSerializable("photo", photo);
                //intent.putExtra("photoBundle",photoBundle);
                //context.startActivity(intent);
                Intent intent = new Intent(context, FullImageView.class);
                //intent.putExtra("photoSrc", photoSrc);
                Bundle photoBundle = new Bundle();
                photoBundle.putSerializable("photos", photos);
                intent.putExtra("position", position);
                intent.putExtra("photoBundle",photoBundle);
                context.startActivity(intent);
            }
        });
    }

//    public String getDemension(String photoSrc){
//        BitmapFactory.Options o = new BitmapFactory.Options();
//        o.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
//        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), photoSrc, o);
//        int w = bmp.getWidth();
//        int h = bmp.getHeight();
//        String result = w + " x " + h;
//        return result;
//    }

//    public String getSize(int s){
//        Bitmap bitmapOrg = BitmapFactory.decodeResource(context.getResources(),s);
//        Bitmap bitmap = bitmapOrg;
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] imageInByte = stream.toByteArray();
//        long lengthbmp = imageInByte.length;
//        long imageInKB = lengthbmp/1024;
//        if (imageInKB/1024.0 > 1) {
//            return Math.round(imageInKB*100.0/1024.0)/100.0 + " MB";
//        }
//        else {
//            return imageInKB + " KB";
//        }
//    }

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



