package com.example.ashwin.popularmovies20;

import android.graphics.Bitmap;

/**
 * Created by ashwin on 8/20/16.
 */
public class ImageItem {

    private Bitmap poster;
    private String title;

    public ImageItem(Bitmap poster, String title){
        super();
        this.poster= poster;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getPoster() {

        return poster;
    }

    public void setPoster(Bitmap poster) {
        this.poster = poster;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
