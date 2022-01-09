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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.telecom.Call;
import android.util.AttributeSet;
import android.util.Log;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FullImageView extends AppCompatActivity {
    ImageView mainImageView;
    Photo photo;
    //ArrayList<String> photoSrc;
    ArrayList<Photo> photos;
    int CurrentPosition = 0;
    ViewPager slider;
    FavoriteDB favoriteDB;
    Menu menuImg;
    //String Src = "";

    Timer mtimer;

    public static final int DS_PHOTO_EDITOR_REQUEST_CODE = 200;

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

        favoriteDB=new FavoriteDB(FullImageView.this);

        Intent intent = getIntent();
        Bundle photoBundle = intent.getBundleExtra("photoBundle");
        photos = (ArrayList<Photo>) photoBundle.getSerializable("photos");
        CurrentPosition = intent.getIntExtra("position", 0);

        slider = findViewById(R.id.slider_sieucapvodichvutru);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, photos);
        slider.setAdapter(adapter);
        slider.setCurrentItem(CurrentPosition);



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
                setFavoriteIcon();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                }
                //Toast.makeText(FullImageView.this, "1", Toast.LENGTH_SHORT).show();
                Uri inputImageUri = Uri.fromFile(new File(photos.get(CurrentPosition).getSrc()));
                Intent intent = new Intent(FullImageView.this, DsPhotoEditorActivity.class);
                intent.setData(inputImageUri);
                intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "DS Photo Editor");
                intent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, Color.BLACK);
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                {
                    intent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR, Color.parseColor("#FF000000"));
                }else {
                    intent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR, Color.parseColor("#FFFFFFFF"));
                }
                startActivityForResult(intent, DS_PHOTO_EDITOR_REQUEST_CODE);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(FullImageView.this);
                myAlertDialog.setTitle("Delete Photo");
                myAlertDialog.setMessage("Do you want to delete it?");
                myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            Photo photo = photos.get(CurrentPosition);
                            int delete = getContentResolver().delete(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(photo.getId())), null, null);
                            if (delete > 0) {
                                photos.remove(CurrentPosition);
                                if (photos.size() < 1) {
                                    onBackPressed();
                                }
                                slider.getAdapter().notifyDataSetChanged();
                            }
                        }catch (Exception e){
                            Log.d("deletedError",e.getMessage());
                        }
                    }});
                myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }});
                myAlertDialog.show();
            }
        });
    }

    private void getView() {
        moreBtn = findViewById(R.id.image_more);
        editBtn = findViewById(R.id.image_edit);
        deleteBtn = findViewById(R.id.image_delete);
        bottomTab = findViewById(R.id.full_image_bottomtab);

    }

    private void autoSlideImage()
    {
        if(photos == null || photos.isEmpty() || slider == null) return;

        if (mtimer == null) {mtimer = new Timer();}
        mtimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = slider.getCurrentItem();
                        int totalItem = photos.size() -1;
                        if (currentItem < totalItem)
                        {
                            currentItem++;
                            slider.setCurrentItem(currentItem);
                        }
                        else
                        {
                            slider.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 3000);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mtimer != null)
        {
            mtimer.cancel();
            mtimer = null;
        }

        favoriteDB.close();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        menuImg=menu;
        getMenuInflater().inflate(R.menu.menu_full_image, menu);
        setFavoriteIcon();
        return super.onCreateOptionsMenu(menu);
    }

    private void setFavoriteIcon() {
        MenuItem item=menuImg.findItem(R.id.menu_favorite);
        if(photos.get(CurrentPosition).favStatus){
            item.setIcon(R.drawable.ic_favorite_red);
        }
        else{
            item.setIcon(R.drawable.ic_favorite_shadow);
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorite:{
                new FavoriteThread().execute(photos.get(CurrentPosition).favStatus);
                break;
            }

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
            case R.id.menu_autoSlide:{
                this.autoSlideImage();
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

    private class FavoriteThread extends AsyncTask <Boolean, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... booleans) {
            boolean isDone;
            if (booleans != null && booleans[0]) {
                isDone = favoriteDB.deleteFavorite(photos.get(CurrentPosition).getId());
            } else {
                isDone = favoriteDB.insertFavorite(photos.get(CurrentPosition).getId());
            }
            return isDone;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                MenuItem menuItem =  menuImg.findItem(R.id.menu_favorite);
                if(photos.get(CurrentPosition).setFavStatus()) {
                    menuItem.setIcon(R.drawable.ic_favorite_red);
                } else {
                    menuItem.setIcon(R.drawable.ic_favorite_shadow);
                }
                //imageSlider.notifyDataSetChanged();
            } else {
                Toast.makeText(FullImageView.this, "An unexpected error has occured. Try again later.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
