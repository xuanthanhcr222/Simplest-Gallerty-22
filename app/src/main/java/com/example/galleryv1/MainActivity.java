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
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
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

//        if (android.os.Build.VERSION.SDK_INT >= 23) {
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_EXTERNAL_STORAGE);
//        }

        if(!checkPermission()){
            requestPermission();
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
            case R.id.menu_darkMode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case R.id.menu_lightMode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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


    //Nguồn tham khảo:
    //https://stackoverflow.com/questions/68480231/i-cant-delete-image-from-gallery-with-android?fbclid=IwAR19Hfjpi5o3eBnBqTW8gr4pUIHICvEXXw2MnQ2AZDTGgrAOzfnjl13GkI8
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
        }
    }
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = 0;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                result = this.getApplication().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                int result1 = this.getApplication().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
            }
        }
        return true;
    }
}


