package com.example.galleryv1;

import java.util.ArrayList;
import java.util.Date;

public class Album {
    private String name;
    private String src;
    private Date createdDate;
    private ArrayList<Photo> photoArrayList;

    public Album(String name, String src, Date createdDate, ArrayList<Photo> photoArrayList) {
        this.name = name;
        this.src = src;
        this.createdDate = createdDate;
        this.photoArrayList = photoArrayList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public ArrayList<Photo> getPhotoArrayList() {
        return photoArrayList;
    }

    public void setPhotoArrayList(ArrayList<Photo> photoArrayList) {
        this.photoArrayList = photoArrayList;
    }

    public String getThumbnail()
    {
        if (photoArrayList.size() == 0)
            return  "";

        int size = photoArrayList.size();
        return photoArrayList.get(size-1).getSrc();
    }

}
