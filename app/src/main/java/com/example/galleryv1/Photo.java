package com.example.galleryv1;

import java.util.ArrayList;
import java.util.Date;

public class Photo {
    private String name;
    private String src;
    private Date createdDate;
    private ArrayList <String> tags;
    private int typeDisplay = 1;

    private static final int TYPE_GRID = 1;
    private static final int TYPE_LIST = 2;

    public Photo(String name, String src, Date createdDate, ArrayList<String> tags) {
        this.name = name;
        this.src = src;
        this.createdDate = createdDate;
        this.tags = tags;
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

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public int getTypeDisplay() {
        return typeDisplay;
    }

    public void setTypeDisplay(int typeDisplay) {
        this.typeDisplay = typeDisplay;
    }

    public static int getTypeGrid() {
        return TYPE_GRID;
    }

    public static int getTypeList() {
        return TYPE_LIST;
    }
}
