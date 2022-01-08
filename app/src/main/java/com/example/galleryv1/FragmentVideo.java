package com.example.galleryv1;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class FragmentVideo extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Video> videos;
    VideoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.videoRecycler);
        recyclerView.setHasFixedSize(true);

        adapter = new VideoAdapter(getContext(), videos, getActivity());
        recyclerView.setAdapter(adapter);
        int numColumn = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numColumn));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getAllVideos();
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            videos = (ArrayList<Video>) bundle.getSerializable("videoList");
//        }
    }

    private void getAllVideos(){
        if (videos != null) {
            videos.clear();
        } else {
            videos = new ArrayList<>();
        }


        String[] videoProjection  = {
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DATE_ADDED,
                MediaStore.Video.VideoColumns.DATE_MODIFIED,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.SIZE,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Video.VideoColumns.MIME_TYPE,
                MediaStore.Video.Thumbnails.DATA
        };

        Uri videosUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor videoCursor = getContext().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videoProjection,
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_ADDED + " DESC" //Sắp xếp theo ngày tạo
        );

        try {
            if (videoCursor != null) {
                videoCursor.moveToFirst();
            }
            do{
//                String name = videoCursor.getString(videoCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DISPLAY_NAME));
                String path = videoCursor.getString(videoCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA));
//                Long createdDateTimeStamp = videoCursor.getLong(videoCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATE_ADDED));
//                Date createdDate = new Date(createdDateTimeStamp*1000);
                String thumb = videoCursor.getString(videoCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));

                Video video = new Video(path, thumb, false) ;

                videos.add(video);

            }while(videoCursor.moveToNext());
            videoCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
