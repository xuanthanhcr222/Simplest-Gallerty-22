package com.example.galleryv1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

    ImageButton addBtn;
    EditText newAlbumNameEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_album_layout,container,false);

        recyclerView=(RecyclerView) view.findViewById(R.id.albumRecycler);

        addBtn = (ImageButton) view.findViewById(R.id.addBtn);



        recyclerView.setHasFixedSize(true);
        adapter=new AlbumAdapter(getContext(), albums);
        recyclerView.setAdapter(adapter);
        int numColumn = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numColumn));

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAddAlbumtDialog();
//                Toast.makeText(getContext(), "Add Album Btn", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAllAlbums();

    }

    private void displayAddAlbumtDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View addAlbumDialogLayout = inflater.inflate(R.layout.custom_add_album_dialog, null);


        newAlbumNameEditText = (EditText) addAlbumDialogLayout.findViewById(R.id.newAlbumNameEditText);


        final AlertDialog.Builder addAlbumDialog = new AlertDialog.Builder(getContext());

        addAlbumDialog.setTitle("Add new album");
        addAlbumDialog.setView(addAlbumDialogLayout);


        addAlbumDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String albumName = newAlbumNameEditText.getText().toString();

                Toast.makeText(getContext(), "new album name: " + albumName, Toast.LENGTH_SHORT).show();
                addNewAlbum(albumName);
            }
        });

        addAlbumDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        addAlbumDialog.show();
    }

    private void addNewAlbum(String albumName)
    {
        String emptyFolderImgSrc = Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" + R.drawable.folder).toString();
        String dcimFolderPath = Environment.getExternalStorageDirectory().toString() + "/DCIM";
        File newFolder = new File(dcimFolderPath + "/" + albumName);
        newFolder.mkdirs();
        Album album = new Album(newFolder.getName(),newFolder.getPath(),emptyFolderImgSrc);
        albums.add(album);
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

        //Lấy ra danh sách album (không trùng lặp) từ albumArrayList
        HashSet<Album> uniqueAlbum = new HashSet<>(albumArrayList);
        albums = new ArrayList<>(uniqueAlbum);
        int countAlbum = albums.size();


        //Thêm ảnh Thumbnail cho từng album
        for (int i = 0; i < countAlbum; i++) {
            String albumPath = albums.get(i).getSrc();
            String albumName = albums.get(i).getName();

            String[] albumThumbProjection = {"MAX(" + MediaStore.Images.ImageColumns.DATE_MODIFIED + ")",
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
            };

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


        //Vì các album ở trên bắt buộc phải có ảnh
        //Nên đoạn code dưới đây sẽ tìm những album (không có ảnh) trong thư mục DCIM
        //(ví dụ như những album mới tạo)

        String dcimFolderPath = Environment.getExternalStorageDirectory().toString() + "/DCIM";
        File dcimFolder = new File(dcimFolderPath);
        File[] dcimListFiles = dcimFolder.listFiles();


        //Hình nền cho những album rỗng
        String emptyFolderImgSrc = Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" + R.drawable.folder).toString();

        for (File dcimFolderItem : dcimListFiles)
        {
            if(dcimFolderItem.isDirectory())
            {
                File[] dcimFolderItemListFiles = dcimFolderItem.listFiles();
                if(dcimFolderItemListFiles.length == 0)
                {
                    Album album = new Album(dcimFolderItem.getName(), dcimFolderItem.getPath(), emptyFolderImgSrc);
                    albums.add(album);
                }
            }
        }
        Log.d("dcim",dcimFolderPath);
    }
}
