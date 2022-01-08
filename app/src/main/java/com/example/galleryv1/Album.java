package com.example.galleryv1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Album implements Serializable{
    private String name;
    private String src;
    private String thumbnail;


    public Album(String name, String src, String thumbnail) {
        this.name = name;
        this.src = src;
        this.thumbnail = thumbnail;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(name, album.name) && Objects.equals(src, album.src) && Objects.equals(thumbnail, album.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, src, thumbnail);
    }
}
