package com.example.galleryv1;

import static android.Manifest.permission.SET_WALLPAPER;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.telecom.Call;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FullImageView extends AppCompatActivity {
    ImageView mainImageView;
    Photo photo;
    //ArrayList<String> photoSrc;
    ArrayList<Photo> photos;
    int CurrentPosition = 0;
    //String Src = "";

    BottomAppBar bottomTab;
    ImageButton editBtn, moreBtn, deleteBtn;

    final int SET_WALLPAPER = 1;
    final int SET_LOCKSCREEN = 2;
    final int SET_ALL = 3;

    AlertDialog dialog;

    ScaleGestureDetector gesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider);

        getView();

        Intent intent = getIntent();
        Bundle photoBundle = intent.getBundleExtra("photoBundle");
        photos = (ArrayList<Photo>) photoBundle.getSerializable("photos");
        CurrentPosition = intent.getIntExtra("position", 0);

        ViewPager slider = findViewById(R.id.slider_sieucapvodichvutru);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, photos);
        slider.setAdapter(adapter);
        slider.setCurrentItem(CurrentPosition);

        //final LayoutInflater factory = getLayoutInflater();
        //final View view = factory.inflate(R.layout.activity_full_image, null);
        //mainImageView = (ImageView) view.findViewById(R.id.imageViewFull);
        //mainImageView.setImageResource(R.drawable.pause_button);


        slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportActionBar().isShowing()) {
                    bottomTab.setVisibility(View.INVISIBLE);
                    slider.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT));
                } else {
                    bottomTab.setVisibility(View.VISIBLE);
                    slider.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT));
                }
            }
        });

        slider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                CurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FullImageView.this, "1", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(FullImageView.this, EditImageActivity.class);
                //intent.putExtra("path", photos.get(CurrentPosition).getSrc());
                //tartActivityForResult(intent, REQUEST_EDIT_IMAGE);
            }
        });
    }

    private void getView() {
        moreBtn = findViewById(R.id.image_more);
        editBtn = findViewById(R.id.image_edit);
        deleteBtn = findViewById(R.id.image_delete);
        bottomTab = findViewById(R.id.full_image_bottomtab);

    }

    class MyGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        float scale = 1.0F, scaleBeg = 0, scaleEnd = 0;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            mainImageView.setScaleX(scale);
            mainImageView.setScaleY(scale);
            return super.onScale(detector);
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            scaleBeg = scale;
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            scaleEnd = scale;
            super.onScaleEnd(detector);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_full_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_ttct: {

                photo = photos.get(CurrentPosition);

                final AlertDialog.Builder showInfoDialog = new AlertDialog.Builder(this);
                showInfoDialog.setTitle("Image Detail");
                String info = "\nName: " + photo.getName() + "\nCreate At: " + photo.getCreatedDate() +
                        "\nModified At: " + photo.getModifiedDate() + "\nDimension: " + photo.getWidth() + " x " +
                        photo.getHeight() + "\nSize: " + photo.getSize() + "\nSource: " + photo.getSrc();
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
            case R.id.menu_share: {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(photos.get(CurrentPosition).getSrc())));
                intent.setType("image/*");
                startActivity(Intent.createChooser(intent, "Share"));

                break;
            }
            case R.id.menu_wallpaper: {
                new SetWallpaperThread().execute(SET_WALLPAPER);
                break;
            }
            case R.id.menu_lockScreen: {
                new SetWallpaperThread().execute(SET_LOCKSCREEN);
                break;
            }
            case R.id.menu_setAll:{
                new SetWallpaperThread().execute(SET_ALL);
                break;
            }
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        Glide.with(FullImageView.this).load("file://" + photos.get(CurrentPosition))
                .skipMemoryCache(false)
                .into(mainImageView);
    }

    private class SetWallpaperThread extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... integers) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullImageView.this);
            Bitmap bmap2 = BitmapFactory.decodeFile(photos.get(CurrentPosition).getSrc());

            try {
                if (integers[0] == SET_WALLPAPER) {
                    wallpaperManager.setBitmap(bmap2, null, true, WallpaperManager.FLAG_SYSTEM);
                } else if (integers[0] == SET_LOCKSCREEN) {
                    wallpaperManager.setBitmap(bmap2, null, true, WallpaperManager.FLAG_LOCK);
                } else if (integers[0] == SET_ALL) {
                    wallpaperManager.setBitmap(bmap2, null, true, WallpaperManager.FLAG_SYSTEM);
                    wallpaperManager.setBitmap(bmap2, null, true, WallpaperManager.FLAG_LOCK);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }
}
