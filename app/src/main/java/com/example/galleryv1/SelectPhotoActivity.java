package com.example.galleryv1;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;

public class SelectPhotoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Photo> photos;
    private SelectPhotoAdapter adapter;
    private ArrayList<String> addedPhotoSrc;

    FavoriteDB favoriteDB;

    Album album;

    ImageButton addBtn;
    ImageButton cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        addedPhotoSrc = new ArrayList<String>();

        addBtn = (ImageButton) findViewById(R.id.addBtn);
        cancelBtn = (ImageButton) findViewById(R.id.cancelBtn);

        Intent intent = getIntent();
        Bundle albumBundle = intent.getBundleExtra("albumBundle");
        album = (Album) albumBundle.getSerializable("album");

        favoriteDB=new FavoriteDB(SelectPhotoActivity.this);

        getAllPhotos();


        recyclerView=(RecyclerView) findViewById(R.id.imageRecycler);
        recyclerView.setHasFixedSize(true);

        adapter=new SelectPhotoAdapter(this, photos);
        recyclerView.setAdapter(adapter);
        int numColumn = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numColumn));

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addedPhotoSrc = adapter.getAddedPhotoSrc();

                try {

                    String albumSrc = album.getSrc();
                    for (String src: addedPhotoSrc)
                    {
                        int index = src.lastIndexOf("/");
                        String name = src.substring(index + 1);


                        File from = new File(src);
                        File to = new File(albumSrc + "/" + name);
                        Files.move(from.toPath(), to.toPath());


//                        copyOrMoveFile(from,to, true);
//                        from.renameTo(to);
//                        Files.move(from.toPath(), to.toPath());
//                        String newAlbumPath = Environment.getExternalStorageDirectory().toString() + "/DCIM" + "/demo123456";
//                        File to = new File(newAlbumPath+"/" + name);
//
//                        if(from.exists()){
//                            if(!to.exists()){
//                                Files.move(from.toPath(), to.toPath());
////                                from.renameTo(to);
//                            }
//
//
//                        }
                    }

                    Intent intent = new Intent(SelectPhotoActivity.this, AlbumDetail.class);
                    Bundle albumBundle = new Bundle();
                    albumBundle.putSerializable("album", album);
                    intent.putExtra("albumBundle",albumBundle);
                    startActivity(intent);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectPhotoActivity.this, AlbumDetail.class);
                Bundle albumBundle = new Bundle();
                albumBundle.putSerializable("album", album);
                intent.putExtra("albumBundle",albumBundle);
                startActivity(intent);
            }
        });
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
//                MediaStore.Images.ImageColumns.RESOLUTION, ////
                MediaStore.Images.Thumbnails.DATA
        };

        Uri imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor imageCursor = SelectPhotoActivity.this.getContentResolver().query(
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

                boolean favStatus= favoriteDB.readFavorite(id);

                Photo photo = new Photo(id, name,src,createdDate, modifiedDate, thumb, width, height, size, favStatus);

                photos.add(photo);

            }while(imageCursor.moveToNext());
            imageCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}