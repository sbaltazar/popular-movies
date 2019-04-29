package com.sbaltazar.popularmovies.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.sbaltazar.popularmovies.data.repository.MovieRepository;
import com.sbaltazar.popularmovies.data.entity.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository mRepository;

    private LiveData<List<Movie>> mAllMovies;

    public MovieViewModel(Application application) {
        super(application);
        mRepository = new MovieRepository(application);
        mAllMovies = mRepository.getAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return mAllMovies;
    }

    public LiveData<Movie> getMovie(int movieId) {
        return mRepository.getMovie(movieId);
    }

    public void insert(Movie movie) {
        mRepository.insert(movie);
    }

    public void delete(int movieId) {
        mRepository.delete(movieId);
    }
}
