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
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class FragmentAlbum extends Fragment {



    private RecyclerView recyclerView;
    private ArrayList<Album> albums;
    private AlbumAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_album_layout,container,false);

        recyclerView=(RecyclerView) view.findViewById(R.id.albumRecycler);


        recyclerView.setHasFixedSize(true);
        adapter=new AlbumAdapter(getContext(), albums);
        recyclerView.setAdapter(adapter);
        int numColumn = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numColumn));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAllAlbums();
    }

    private void getAllAlbums() {
        if (albums != null) {
            albums.clear();
        } else {
            albums = new ArrayList<>();
        }

        ArrayList<Album> albumArrayList = new ArrayList<>();

        String[] albumProjection = {
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DISPLAY_NAME
        };

        Cursor albumCursor = getContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                albumProjection,
                null, // Selection args (none).
                null,
                null
        );

//        Cursor cursor = getContext().getContentResolver().query(allImagesuri, projection, null, null, null);

        try {
            if (albumCursor != null) {
                albumCursor.moveToFirst();
            }
            do {
                String albumName = albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
                String fileName = albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME));
                String filePath = albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));

                int index = filePath.lastIndexOf("/" + fileName);
                String albumPath = filePath.substring(0, index);

                Album album = new Album(albumName, albumPath, "");

                albumArrayList.add(album);


            } while (albumCursor.moveToNext());
            albumCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashSet<Album> uniqueAlbum = new HashSet<>(albumArrayList);

        albums = new ArrayList<>(uniqueAlbum);


//        ArrayList<String> thumbs = new ArrayList<>();

        int countAlbum = albums.size();


        for (int i = 0; i < countAlbum; i++) {
            String albumPath = albums.get(i).getSrc();
            String albumName = albums.get(i).getName();

            String[] albumThumbProjection = {"MAX(" + MediaStore.Images.ImageColumns.DATE_MODIFIED + ")",
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
            };

//            /storage/emulated/0/DCIM
//            /storage/emulated/0/DCIM/Screenshots/Screenshot_20211206-021132_GalleryV1.jpg
//            Screenshot_20211206-021132_GalleryV1.jpg
//            20211204_170045.jpg
            Cursor albumThumbCursor = getContext().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    albumThumbProjection,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + " = ? ",
                    new String[]{albumName},
                    null
            );

            try {
                if (albumThumbCursor != null) {
                    albumThumbCursor.moveToFirst();
                }
                do {
//                    String name = albumThumbCursor.getString(albumThumbCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME));
                    String path = albumThumbCursor.getString(albumThumbCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
//                    thumbs.add(path);
                    albums.get(i).setThumbnail(path);
                } while (albumThumbCursor.moveToNext());
                albumThumbCursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
