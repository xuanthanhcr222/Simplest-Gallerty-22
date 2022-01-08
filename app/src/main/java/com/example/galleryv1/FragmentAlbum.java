package com.example.galleryv1;

import android.os.Bundle;
import android.os.Environment;
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

        albums = new ArrayList<>();


        String externalStoragePath = Environment.getExternalStorageDirectory().toString()+"/DCIM";

        try {
            albumDirectoryBrowsing(externalStoragePath,albums);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        File directory = new File(externalStoragePath);
//        File[] folders = directory.listFiles();
//
//
//        for (File folder : folders)
//        {
//            if(folder.isDirectory())
//            {
//
//                if (folder.getName().equals(".thumbnails"))
//                {
//                    Log.e("Thump", folder.getName());
//                    continue;
//                }
//
//                Log.e("Album", folder.toString() +  " --> alumn");
//                DateFormat df =new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
//                try {
//                    BasicFileAttributes folderDetail = Files.readAttributes( folder.toPath(), BasicFileAttributes.class);
//
//                    Date folderCreateDate = df.parse(df.format(folderDetail.creationTime().toMillis()));
//
//                    File[] files = folder.listFiles();
//
//                    ArrayList<Photo> photos = new ArrayList<>();
//                    for (File file : files)
//                    {
//                        BasicFileAttributes fileDetail = Files.readAttributes( file.toPath(), BasicFileAttributes.class);
//                        Date photoCreateDate = df.parse(df.format(folderDetail.creationTime().toMillis()));
//
//                        photos.add( new Photo(file.getName(),file.getAbsolutePath(),photoCreateDate,new ArrayList<>()));
//
//
//
//                    }
//
//                    albums.add(new Album(folder.getName(),folder.getAbsolutePath(),folderCreateDate,photos));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
    }

    void albumDirectoryBrowsing(String directoryStrPath, ArrayList<Album>albums) throws IOException, ParseException {

        //Tạo thư mục từ đường dẫn
        File directory = new File(directoryStrPath);

        //Date format
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        //Lấy các thuộc tính của directory;
        BasicFileAttributes directoryDetail = Files.readAttributes(directory.toPath(), BasicFileAttributes.class);
        Date albumCreateDate = df.parse(df.format(directoryDetail.creationTime().toMillis()));


        //Tạo ArrayList mới
        ArrayList<Photo> photos = new ArrayList<>();

        //Tạo Album mới
        Album album = new Album(directory.getName(),directory.getAbsolutePath(),albumCreateDate, photos);


        // Lấy danh sách các item ở trong directory -> tạo thành File[] folders

        File[] folder = directory.listFiles();

        //Duyệt qua từng item con

        //Nếu như là 1 directory thì
        //Bỏ qua các ".thumbnails"
        //

        //Ngược lại, Nếu như là file thì kiểm tra nếu là file (jpg, png) -> add vào photos của album hiện tại

        for (File item : folder) {
            if (item.isDirectory())
            {
                if (item.getName().equals(".thumbnails")) {
                    continue;
                }
                else
                {
                    Log.e("foldername: ",item.getName());

                    albumDirectoryBrowsing(item.getAbsolutePath(),albums);
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

        albums.add(album);
    }
}
