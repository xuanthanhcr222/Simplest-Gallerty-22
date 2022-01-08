package com.example.galleryv1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class AlbumDetail extends AppCompatActivity {

    Album album;
    private RecyclerView recyclerView;
    private ArrayList<Photo>photos;
    private PhotoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_details);

        Intent intent = getIntent();

        if(intent.hasExtra("albumBundle")) {
            Bundle albumBundle = intent.getBundleExtra("albumBundle");
            album = (Album) albumBundle.getSerializable("album");
        }
        else {
            Toast.makeText(this, "No Data!", Toast.LENGTH_SHORT).show();
        }

        getAllPhotosOfAlbum();

        recyclerView=(RecyclerView) findViewById(R.id.imageRecycler);
        recyclerView.setHasFixedSize(true);

        adapter=new PhotoAdapter(this, photos);
        recyclerView.setAdapter(adapter);
        int numColumn = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numColumn));

    }

    private void getAllPhotosOfAlbum(){
        if (photos != null) {
            photos.clear();
        } else {
            photos = new ArrayList<>();
        }

        String[] imageProjection  = {
                MediaStore.Images.ImageColumns.DATA, // path của photo
                MediaStore.Images.ImageColumns.DISPLAY_NAME, // tên hiển thị photo
                MediaStore.Images.ImageColumns.DATE_MODIFIED, // ngày chỉnh sửa
                MediaStore.Images.ImageColumns.DATE_ADDED, // ngày add
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, // Tên thư mục chứa
                MediaStore.Images.ImageColumns.SIZE, // kích thước
                MediaStore.Images.ImageColumns.WIDTH, //
                MediaStore.Images.ImageColumns.HEIGHT, //
//                MediaStore.Images.ImageColumns.RESOLUTION, ////
                MediaStore.Images.Thumbnails.DATA
        };

        Uri imagesUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor imageCursor = this.getApplication().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageProjection,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + " = ? ",
                new String[]{album.getName()},
                MediaStore.Images.ImageColumns.DATE_ADDED + " DESC" //Sắp xếp theo ngày tạo
        );

        try {
            if (imageCursor != null) {
                imageCursor.moveToFirst();
            }
            do{
                String name = imageCursor.getString(imageCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME));
                String src = imageCursor.getString(imageCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));

                Long createdDateTimeStamp = imageCursor.getLong(imageCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_ADDED));
                Long modifiedDateTimeStamp = imageCursor.getLong(imageCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED));
                Date createdDate = new Date(createdDateTimeStamp*1000);
                Date modifiedDate = new Date(modifiedDateTimeStamp*1000);

                String thumb = imageCursor.getString(imageCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
                int width = imageCursor.getInt(imageCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.WIDTH));
                int height = imageCursor.getInt(imageCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.HEIGHT));

                int size = imageCursor.getInt(imageCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE));

                Photo photo = new Photo(name,src,createdDate, modifiedDate, thumb, width, height, size);
                photos.add(photo);

            }while(imageCursor.moveToNext());
            imageCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}