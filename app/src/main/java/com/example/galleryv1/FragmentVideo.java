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

public class FragmentVideo extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Video> arr;
    VideoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.videoRecycler);
        recyclerView.setHasFixedSize(true);
        arr = new ArrayList<>();

        adapter = new VideoAdapter(getContext(), arr, getActivity());
        recyclerView.setAdapter(adapter);
        int numColumn = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numColumn));
        fetchVideosFromGallery();
        return view;
    }

    private void fetchVideosFromGallery() {
        Uri uri;
        Cursor cursor;
        int colIndexData, colIndexFolderName, colId, thumb;
        String pathImage = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Thumbnails.DATA};
        String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        colIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        //colIndexFolderName=cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        //colId=cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
        thumb = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

        while (cursor.moveToNext()) {
            pathImage = cursor.getString(colIndexData);

            Video video = new Video();
            video.setSelected(false);
            video.setPath(pathImage);
            video.setThumb(cursor.getString(thumb));

            arr.add(video);

        }
    }
}
