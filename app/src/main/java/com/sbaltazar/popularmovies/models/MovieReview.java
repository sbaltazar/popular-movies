package com.sbaltazar.popularmovies.models;

public class MovieReview {

    private String author;
    private String content;
    private int movieId;

    public MovieReview(String author, String content, int movieId) {
        this.author = author;
        this.content = content;
        this.movieId = movieId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
