package com.sbaltazar.popularmovies.models;

public class MovieTrailer {

    private String id;
    private String title;
    private int movieId;
    private String url;

    public MovieTrailer(String id, String title, int movieId, String url) {
        this.id = id;
        this.title = title;
        this.movieId = movieId;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
