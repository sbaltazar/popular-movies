package com.sbaltazar.popularmovies.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Movie implements Parcelable {

    private String title;
    private String imageUrl;
    private Bitmap image;
    private Date releaseDate;
    private double voteAverage;
    private String synopsis;

    public Movie(String title, String imageUrl, Bitmap image, Date releaseDate, double voteAverage, String synopsis) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.image = image;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.synopsis = synopsis;
    }

    private Movie(Parcel in) {
        title = in.readString();
        imageUrl = in.readString();
        image = in.readParcelable(null);
        releaseDate = (Date) in.readSerializable();
        voteAverage = in.readDouble();
        synopsis = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeParcelable(image, flags);
        dest.writeSerializable(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeString(synopsis);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
