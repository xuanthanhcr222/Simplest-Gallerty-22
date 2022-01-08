package com.example.galleryv1;

import java.io.Serializable;

public class Video implements Serializable {
    String path,thumb;
    boolean selected;

    public Video(String path, String thumb, boolean selected) {
        this.path = path;
        this.thumb = thumb;
        this.selected = selected;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}