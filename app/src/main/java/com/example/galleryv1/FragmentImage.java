package com.example.galleryv1;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class  FragmentImage extends Fragment {



    private RecyclerView recyclerView;
    private ArrayList<Photo>photos;
    private PhotoAdapter adapter;
    FavoriteDB favoriteDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_image_layout,container,false);
        recyclerView=(RecyclerView) view.findViewById(R.id.imageRecycler);
        recyclerView.setHasFixedSize(true);

        adapter=new PhotoAdapter(getContext(), photos);
        recyclerView.setAdapter(adapter);
        int numColumn = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numColumn));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        favoriteDB=new FavoriteDB(getContext());
        getAllPhotos();

    }

    private void getAllPhotos(){
        if (photos != null) {
            photos.clear();
        } else {
            photos = new ArrayList<>();
        }

        String[] imageProjection  = {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA, // path của photo
                MediaStore.Images.ImageColumns.DISPLAY_NAME, // tên hiển thị photo
                MediaStore.Images.ImageColumns.DATE_MODIFIED, // ngày chỉnh sửa
                MediaStore.Images.ImageColumns.DATE_ADDED, // ngày add
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, // Tên thư mục chứa
                MediaStore.Images.ImageColumns.SIZE, // kích thước
                MediaStore.Images.ImageColumns.WIDTH, //
                MediaStore.Images.ImageColumns.HEIGHT, //
                MediaStore.Images.Thumbnails.DATA
        };

        Uri imagesUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor imageCursor = getContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageProjection,
                null,
                null,
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


                int id = imageCursor.getInt(imageCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID));

                boolean favStatus=favoriteDB.readFavorite(id);
                Photo photo = new Photo(id, name,src,createdDate, modifiedDate, thumb, width, height, size, favStatus);

                photos.add(photo);

            }while(imageCursor.moveToNext());
            imageCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
