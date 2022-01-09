package com.example.galleryv1;

import android.provider.MediaStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Photo implements Serializable {
    private int id;
    private String name;
    private String src;
    private Date createdDate;
    private Date modifiedDate;
    private String thumb;
    public boolean favStatus;

    private int width;
    private int height;
    private int size;

    private int typeDisplay = 1;

    private static final int TYPE_GRID = 1;
    private static final int TYPE_LIST = 2;

    public Photo(int id, String name, String src, Date createdDate, Date modifiedDate, String thumb, int width, int height, int size, boolean favStatus) {
        this.id = id;
        this.name = name;
        this.src = src;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.thumb = thumb;
        this.width = width;
        this.height = height;
        this.size = size;
        this.favStatus=favStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean setFavStatus(){
        this.favStatus=!this.favStatus;
        return this.favStatus;
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
