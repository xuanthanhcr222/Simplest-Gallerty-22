package com.example.galleryv1;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

class ViewPagerAdapter extends PagerAdapter {
    Context context;
    LayoutInflater mLayoutInflater;
    ArrayList<Photo> photos;
    ScaleGestureDetector gesture;
    ImageView imageView;

    public ViewPagerAdapter(Context context, ArrayList<Photo> photos) {
        this.context = context;
        this.photos = photos;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @NonNull
    @Override //Class ViewPagerAdapter
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemview = mLayoutInflater.inflate(
                R.layout.activity_full_image, container, false);
        imageView = (ImageView) itemview.findViewById(R.id.imageViewFull);
        Glide.with(context).load("file://" + photos.get(position).getThumb())
                .skipMemoryCache(false)
                .into(imageView);
        container.addView(itemview);
        Context context = container.getContext();
        ImageMatrixTouchHandler imageMatrixTouchHandler = new ImageMatrixTouchHandler(context);
        imageView.setOnTouchListener(imageMatrixTouchHandler);
        return imageView;
    }

    class MyGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        float scale = 1.0F, scaleBeg = 0, scaleEnd = 0;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            imageView.setScaleX(scale);
            imageView.setScaleY(scale);
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
}
