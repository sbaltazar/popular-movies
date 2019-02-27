package com.sbaltazar.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sbaltazar.popularmovies.R;
import com.sbaltazar.popularmovies.adapters.MovieAdapter;
import com.sbaltazar.popularmovies.models.Movie;
import com.sbaltazar.popularmovies.utilities.MovieJsonUtils;
import com.sbaltazar.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

public class MovieDiscoveryActivity extends AppCompatActivity implements MovieAdapter.MoviePosterClickListener {

    private static final String TAG = MovieDiscoveryActivity.class.getSimpleName();

    public static final int NUMBER_OF_COLUMNS = 3;

    private RecyclerView mMovieRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadIndicator;
    private LinearLayout mNoInternetHelp;
    private Button mFetchMoviesButton;

    private URL mCurrentMovieUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_discovery);

        mMovieRecyclerView = findViewById(R.id.rv_movies);
        mLoadIndicator = findViewById(R.id.pb_load_movies_indicator);
        mNoInternetHelp = findViewById(R.id.ll_no_internet);
        mFetchMoviesButton = findViewById(R.id.btn_fetch_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);

        mMovieRecyclerView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(this);
        mMovieRecyclerView.setAdapter(mMovieAdapter);

        // Popular movies are fetch by default
        mCurrentMovieUrl = NetworkUtils.getPopularMovieUrl();

        mFetchMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceHasInternetConnection()) {
                    new FetchMoviesTask().execute(mCurrentMovieUrl);
                    showNoInternetHelp(false);
                } else {
                    Toast.makeText(v.getContext(), R.string.no_internet_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (deviceHasInternetConnection()) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.title_popular_movies);
            }
            new FetchMoviesTask().execute(mCurrentMovieUrl);
        } else {
            showNoInternetHelp(true);
        }
    }

    @Override
    public void onMoviePosterClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie_discovery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_sort_by_popular:
                if (deviceHasInternetConnection()) {
                    mCurrentMovieUrl = NetworkUtils.getPopularMovieUrl();
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.title_popular_movies);
                    }
                    new FetchMoviesTask().execute(mCurrentMovieUrl);
                }
                showNoInternetHelp(!deviceHasInternetConnection());
                return true;

            case R.id.action_sort_by_top_rated:
                if (deviceHasInternetConnection()) {
                    mCurrentMovieUrl = NetworkUtils.getTopRatedMovieUrl();
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.title_top_rated_movies);
                    }
                    new FetchMoviesTask().execute(mCurrentMovieUrl);
                }
                showNoInternetHelp(!deviceHasInternetConnection());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showNoInternetHelp(boolean state) {
        mNoInternetHelp.setVisibility(state ? View.VISIBLE : View.INVISIBLE);
        mMovieRecyclerView.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
    }

    private boolean deviceHasInternetConnection() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        return (activeNetwork != null && activeNetwork.isConnected());
    }

    public class FetchMoviesTask extends AsyncTask<URL, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            mMovieRecyclerView.setVisibility(View.INVISIBLE);
            mLoadIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(URL... urls) {

            URL movieUrl = urls[0];

            try {
                String jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieUrl);

                return MovieJsonUtils.getMoviesFromJson(jsonMoviesResponse);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Cannot get response from movies url");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Cannot parse the movies JSON response");
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e(TAG, "Cannot parse the movie's release date");
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
