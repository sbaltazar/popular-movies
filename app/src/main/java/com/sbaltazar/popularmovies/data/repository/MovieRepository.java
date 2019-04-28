package com.sbaltazar.popularmovies.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.sbaltazar.popularmovies.data.MovieDatabase;
import com.sbaltazar.popularmovies.data.dao.MovieDao;
import com.sbaltazar.popularmovies.models.Movie;

import java.util.List;

public class MovieRepository {

    private MovieDao mDao;
    private LiveData<List<Movie>> mAllMovies;

    public MovieRepository(Application application) {
        MovieDatabase db = MovieDatabase.getDatabase(application);
        mDao = db.movieDao();
        mAllMovies = mDao.getAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return mAllMovies;
    }

    public void insert(Movie movie){
        new insertTask(mDao).execute(movie);
    }

    private static class insertTask extends AsyncTask<Movie, Void, Void>{

        private MovieDao mTaskDao;

        insertTask(MovieDao dao) {
            mTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            mTaskDao.insert(movies[0]);
            return null;
        }
    }
}
