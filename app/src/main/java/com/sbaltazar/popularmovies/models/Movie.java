package com.sbaltazar.popularmovies.models;

import android.graphics.Bitmap;

public class Movie {

    private String title;
    private String imageUrl;
    private Bitmap image;

    public Movie() {
    }

    public Movie(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public Movie(String title, String imageUrl, Bitmap image) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
