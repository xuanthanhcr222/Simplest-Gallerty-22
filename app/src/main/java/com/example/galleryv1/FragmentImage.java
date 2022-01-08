package com.example.galleryv1;

import android.database.Cursor;
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
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    void photoDirectoryBrowsing(File[] folder, ArrayList<Photo>photos) throws IOException, ParseException {
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");

        for (File item : folder) {
            if (item.isDirectory())
            {
                if (item.getName().equals(".thumbnails")) {
                    continue;
                }
                else
                {
                    photoDirectoryBrowsing(item.listFiles(),photos);
                }
            }
            else
            {
                String fileName = item.getName();
                String extension = fileName.substring(fileName.lastIndexOf("."));

                if(extension.equalsIgnoreCase(".jpg") ||
                        extension.equalsIgnoreCase(".png"))
                {

                    BasicFileAttributes fileDetail = Files.readAttributes(item.toPath(), BasicFileAttributes.class);
                    Date photoCreateDate = df.parse(df.format(fileDetail.creationTime().toMillis()));

                    Log.e("filename: ",item.getName());
                    photos.add(new Photo(item.getName(), item.getAbsolutePath(), photoCreateDate, new ArrayList<>()));
                }

            }
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photos= new ArrayList<>();


        String externalStoragePath = Environment.getExternalStorageDirectory().toString()+"/DCIM";
        File directory = new File(externalStoragePath);

        File[] folders = directory.listFiles();

        try {
            photoDirectoryBrowsing(folders,photos);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        int curIndex = -1;
//        for (File item : folders) {
//
//            DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
//            if (item.isDirectory()) {
//
//                if (item.getName().equals(".thumbnails"))
//                {
//                    Log.e("Thump", item.getName());
//                    continue;
//                }
//
//                Log.e("Album", item.toString() + " --> image");
//                try {
//                    BasicFileAttributes itemDetail = Files.readAttributes(item.toPath(), BasicFileAttributes.class);
//
//                    Date itemCreateDate = df.parse(df.format(itemDetail.creationTime().toMillis()));
//
//                    File[] files = item.listFiles();
//
//                    for (File file : files) {
//                        BasicFileAttributes fileDetail = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
//                        Date photoCreateDate = df.parse(df.format(itemDetail.creationTime().toMillis()));
//
//                        Log.e("filename: ",file.getName());
//                        photos.add(new Photo(file.getName(), file.getAbsolutePath(), photoCreateDate, new ArrayList<>()));
//                        curIndex++;
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            else
//            {
//                BasicFileAttributes itemDetail = null;
//                try {
//                    itemDetail = Files.readAttributes(item.toPath(), BasicFileAttributes.class);
//                    Date photoCreateDate = df.parse(df.format(itemDetail.creationTime().toMillis()));
//
//                    curIndex++;
//                    photos.add(new Photo(item.getName(), item.getAbsolutePath(), photoCreateDate, new ArrayList<>()));
//
//                } catch (IOException | ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}
