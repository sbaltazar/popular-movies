package com.sbaltazar.popularmovies.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sbaltazar.popularmovies.R;
import com.sbaltazar.popularmovies.adapters.TrailerAdapter;
import com.sbaltazar.popularmovies.models.Movie;
import com.sbaltazar.popularmovies.models.MovieTrailer;
import com.sbaltazar.popularmovies.utilities.MovieJsonUtils;
import com.sbaltazar.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerClickListener {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mRating;
    private TextView mSynopsis;
    private ImageView mPoster;
    private RecyclerView mTrailerRecyclerView;

    private TrailerAdapter mTrailerAdapter;

    private URL mMovieTrailersUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitle = findViewById(R.id.tv_movie_title);
        mReleaseDate = findViewById(R.id.tv_movie_release_date);
        mRating = findViewById(R.id.tv_movie_rating);
        mSynopsis = findViewById(R.id.tv_movie_synopsis);
        mPoster = findViewById(R.id.iv_movie_poster);
        mTrailerRecyclerView = findViewById(R.id.rv_movie_trailers);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());

        mTrailerAdapter = new TrailerAdapter(this);

        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mTrailerRecyclerView.setLayoutManager(layoutManager);
        mTrailerRecyclerView.addItemDecoration(itemDecoration);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_movie_details);
        }

        if (getIntent() != null && getIntent().hasExtra("movie")) {

            Movie movie = getIntent().getParcelableExtra("movie");

            if (movie != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(movie.getReleaseDate());

                // Setting movie details on UI
                mTitle.setText(movie.getTitle());
                mReleaseDate.setText(String.format("%s", cal.get(Calendar.YEAR)));
                mRating.setText(String.format(Locale.US, "%.1f / 10", movie.getVoteAverage()));
                mSynopsis.setText(movie.getSynopsis());
                mPoster.setImageBitmap(movie.getImage());

                mMovieTrailersUrl = NetworkUtils.getMovieTrailerUrl(movie.getId());

                if (mMovieTrailersUrl != null) {
                    new FetchMovieTrailersTask(this, mTrailerAdapter).execute(mMovieTrailersUrl);
                }

                Log.d(TAG, "Movie trailer id " + movie.getId());
            } else {
                finish();
            }
        }
    }

    @Override
    public void onTrailerClick(MovieTrailer trailer) {
        Uri playTrailerUri = NetworkUtils.getYoutubeUriFromVideoKey(trailer.getUrl());

        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, playTrailerUri);

        if (trailerIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(trailerIntent);
        } else {

            playTrailerUri = NetworkUtils.getWebUriFromVideoKey(trailer.getUrl());
            trailerIntent = new Intent(Intent.ACTION_VIEW, playTrailerUri);

            if(trailerIntent.resolveActivity(getPackageManager()) != null){
                startActivity(trailerIntent);
            } else {
                Toast.makeText(this, "No tiene youtube instalado", Toast.LENGTH_LONG).show();
            }
        }
    }

    private static class FetchMovieTrailersTask extends AsyncTask<URL, Void, List<MovieTrailer>> {

        private WeakReference<MovieDetailActivity> activityReference;

        private TrailerAdapter trailerAdapter;

        FetchMovieTrailersTask(MovieDetailActivity context, TrailerAdapter adapter) {
            activityReference = new WeakReference<>(context);
            trailerAdapter = adapter;
        }

        @Override
        protected List<MovieTrailer> doInBackground(URL... urls) {

            URL trailerUrl = urls[0];

            try {

                String jsonMovieTrailersResponse = NetworkUtils.getResponseFromHttpUrl(trailerUrl);

                return MovieJsonUtils.getMovieTrailersFromJson(jsonMovieTrailersResponse);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Cannot get response from movie trailers url");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Cannot parse the movie trailers JSON response");
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<MovieTrailer> movieTrailers) {

            MovieDetailActivity activity = activityReference.get();

            if (activity == null || activity.isFinishing()) return;

            if (movieTrailers != null) {
                trailerAdapter.setTrailers(movieTrailers);
            }
        }
    }
}
