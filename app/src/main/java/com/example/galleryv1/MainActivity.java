package com.example.galleryv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


//    ArrayList <Album> albums;
//    ArrayList <Photo> photos;
//    ArrayList <Video> videos;

    private SectionPageAdapter sectionPageAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.Theme_Dark);
        }else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.Theme_Light);
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button btn1 = (Button) findViewById(R.id.btn1);
//
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, full_image_view2.class);
//                startActivity(intent);
//            }
//        });

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_EXTERNAL_STORAGE);
        }


//        getAllMedia();

        sectionPageAdapter=new SectionPageAdapter(getSupportFragmentManager());
        viewPager=(ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);
//
        TabLayout tabLayout=(TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }


    private void setupViewPager(ViewPager viewPager){
        FragmentImage photoFrag = new FragmentImage();
//        Bundle photoBundle = new Bundle();
//        photoBundle.putSerializable("photoList", photos);
//        photoFrag.setArguments(photoBundle);


        FragmentVideo videoFrag = new FragmentVideo();
//        Bundle videoBundle = new Bundle();
//        videoBundle.putSerializable("videoList", videos);
//        videoFrag.setArguments(videoBundle);


        SectionPageAdapter adapter=new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(photoFrag,"photo");
        adapter.addFragment(videoFrag,"Video");
        adapter.addFragment(new FragmentAlbum(),"Album");
        viewPager.setAdapter(adapter);
    }

    private void sendPhotosToPhotoFrag(ArrayList <Photo> photos,  FragmentImage photoFrag)
    {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_xml,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_sort:
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_darkMode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case R.id.menu_lightMode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.menu_trashBin:
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_about:
                final AlertDialog.Builder showInfoDialog = new AlertDialog.Builder(this);
                showInfoDialog.setTitle("Simplest Gallery");
                String info = "Group 22\n\n19120321: Lê Thị Ngọc Như\n19120346: Phan Vũ Trúc Quỳnh\n" +
                        "19120347: Trần Ngọc Sang\n19120368: Đỗ Xuân Thanh\n19120512: Nguyễn Đình Hiệu";
                showInfoDialog.setMessage(info);
                showInfoDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                showInfoDialog.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    public void getAllMedia()
//    {
//        if (photos != null) {
//            photos.clear();
//        } else {
//            photos = new ArrayList<>();
//        }
//        if (videos != null) {
//            videos.clear();
//        } else {
//            videos = new ArrayList<>();
//        }
//
//        String[] imageProjection  = {
//                MediaStore.Images.ImageColumns.DATA, // path của photo
//                MediaStore.Images.ImageColumns.DISPLAY_NAME, // tên hiển thị photo
//                MediaStore.Images.ImageColumns.DATE_MODIFIED, // ngày chỉnh sửa
//                MediaStore.Images.ImageColumns.DATE_ADDED, // ngày add
//                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, // Tên thư mục chứa
//                MediaStore.Images.ImageColumns.SIZE, // kích thước
//                MediaStore.Images.ImageColumns.WIDTH, //
//                MediaStore.Images.ImageColumns.HEIGHT, //
//                MediaStore.Images.ImageColumns.RESOLUTION, ////
//                MediaStore.Images.Thumbnails.DATA
//        };
//
//        Uri imagesUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        Cursor imageCursor = this.getApplication().getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                imageProjection,
//                null,
//                null,
//                MediaStore.Images.ImageColumns.DATE_ADDED + " DESC" //Sắp xếp theo ngày tạo
//        );
//
//        try {
//            if (imageCursor != null) {
//                imageCursor.moveToFirst();
//            }
//            do{
//                String name = imageCursor.getString(imageCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME));
//                String src = imageCursor.getString(imageCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
//
//                Long createdDateTimeStamp = imageCursor.getLong(imageCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_ADDED));
//                Long modifiedDateTimeStamp = imageCursor.getLong(imageCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED));
//                Date createdDate = new Date(createdDateTimeStamp*1000);
//                Date modifiedDate = new Date(createdDateTimeStamp*1000);
//
//                String thumb = imageCursor.getString(imageCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
//                int width = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.WIDTH);
//                int height = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.HEIGHT);
//                String resolution = imageCursor.getString(imageCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.RESOLUTION));
//                int size = imageCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE);
//
//                Photo photo = new Photo(name,src,createdDate, modifiedDate, thumb, width, height, resolution, size);
//                photos.add(photo);
//
//            }while(imageCursor.moveToNext());
//            imageCursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        String[] videoProjection  = {
//                MediaStore.Video.VideoColumns.DATA,
//                MediaStore.Video.VideoColumns.DATE_ADDED,
//                MediaStore.Video.VideoColumns.DATE_MODIFIED,
//                MediaStore.Video.VideoColumns.DURATION,
//                MediaStore.Video.VideoColumns.DISPLAY_NAME,
//                MediaStore.Video.VideoColumns.SIZE,
//                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
//                MediaStore.Video.VideoColumns.MIME_TYPE,
//                MediaStore.Video.Thumbnails.DATA
//        };
//
//        Uri videosUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        Cursor videoCursor = this.getApplication().getContentResolver().query(
//                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                videoProjection,
//                null,
//                null,
//                MediaStore.Images.ImageColumns.DATE_ADDED + " DESC" //Sắp xếp theo ngày tạo
//        );
//
//        try {
//            if (videoCursor != null) {
//                videoCursor.moveToFirst();
//            }
//            do{
////                String name = videoCursor.getString(videoCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DISPLAY_NAME));
//                String path = videoCursor.getString(videoCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA));
////                Long createdDateTimeStamp = videoCursor.getLong(videoCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATE_ADDED));
////                Date createdDate = new Date(createdDateTimeStamp*1000);
//                String thumb = videoCursor.getString(videoCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
//
//                Video video = new Video(path, thumb, false) ;
//
//                videos.add(video);
//
//            }while(videoCursor.moveToNext());
//            videoCursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
//    }
}
