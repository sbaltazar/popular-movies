package com.sbaltazar.popularmovies.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.sbaltazar.popularmovies.R;
import com.sbaltazar.popularmovies.adapters.MovieAdapter;
import com.sbaltazar.popularmovies.models.Movie;
import com.sbaltazar.popularmovies.utilities.MovieJsonUtils;
import com.sbaltazar.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MovieDiscoveryActivity extends AppCompatActivity implements MovieAdapter.MoviePosterClickListener {

    private static final String TAG = MovieDiscoveryActivity.class.getSimpleName();

    public static final int NUMBER_OF_COLUMNS = 3;

    private RecyclerView mMovieRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_discovery);

        mMovieRecyclerView = findViewById(R.id.rv_movies);
        mLoadIndicator = findViewById(R.id.pb_load_movies_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);

        mMovieRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this);

        mMovieRecyclerView.setAdapter(mMovieAdapter);

        new FetchPopularMoviesTask().execute();
    }

    @Override
    public void onMoviePosterClick(String title) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, title);
        startActivity(intent);
    }

    public class FetchPopularMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            mMovieRecyclerView.setVisibility(View.INVISIBLE);
            mLoadIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {

            URL popularMoviesUrl = NetworkUtils.getPopularMovieUrl();

            try {
                String jsonPopularMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(popularMoviesUrl);

                return MovieJsonUtils.getMoviesFromJson(jsonPopularMoviesResponse);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Cannot get response from popular movies url");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Cannot parse the popular movies JSON response");
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {

            mLoadIndicator.setVisibility(View.INVISIBLE);
            mMovieRecyclerView.setVisibility(View.VISIBLE);

            if (movies != null) {
                mMovieAdapter.setMovies(movies);
            }
        }
    }
}
