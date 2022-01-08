package com.example.galleryv1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class FragmentImage extends Fragment {



    private RecyclerView recyclerView;
    private ArrayList<Photo>photos;
    private PhotoAdapter adapter;

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

        photos=new ArrayList<>();

        photos.add( new Photo("Photo 1",R.drawable.ace,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.android001,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.android002,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.android_circle_logo,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.apple,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.background01,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.background02,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.background03,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.background04,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.background05,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.beach,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.buffalo,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.call,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.cat,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.cat1,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.dog1,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.haruno_sakura,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.luffy_ace,new Date(2020,1,1),new ArrayList<>()));
        photos.add( new Photo("Photo 1",R.drawable.sakura,new Date(2020,1,1),new ArrayList<>()));
    }
}
