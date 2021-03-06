package com.sbaltazar.popularmovies.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sbaltazar.popularmovies.R;
import com.sbaltazar.popularmovies.adapters.ReviewAdapter;
import com.sbaltazar.popularmovies.adapters.TrailerAdapter;
import com.sbaltazar.popularmovies.data.entity.Movie;
import com.sbaltazar.popularmovies.data.entity.MovieReview;
import com.sbaltazar.popularmovies.data.entity.MovieTrailer;
import com.sbaltazar.popularmovies.data.viewmodel.MovieViewModel;
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
    private Button mFavorite;
    private ImageView mPoster;
    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private URL mMovieTrailersUrl;
    private URL mMovieReviewsUrl;

    private MovieViewModel mViewModel;

    private boolean isMovieFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitle = findViewById(R.id.tv_movie_title);
        mReleaseDate = findViewById(R.id.tv_movie_release_date);
        mRating = findViewById(R.id.tv_movie_rating);
        mSynopsis = findViewById(R.id.tv_movie_synopsis);
        mFavorite = findViewById(R.id.btn_favorite);
        mPoster = findViewById(R.id.iv_movie_poster);
        mTrailerRecyclerView = findViewById(R.id.rv_movie_trailers);
        mReviewRecyclerView = findViewById(R.id.rv_movie_reviews);

        mViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration trailerItemDecoration = new DividerItemDecoration(this, trailerLayoutManager.getOrientation());

        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration reviewItemDecoration = new DividerItemDecoration(this, reviewLayoutManager.getOrientation());
        reviewItemDecoration.setDrawable(getResources().getDrawable(R.drawable.border_outline));

        mTrailerAdapter = new TrailerAdapter(this);
        mReviewAdapter = new ReviewAdapter();

        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
        mTrailerRecyclerView.addItemDecoration(trailerItemDecoration);

        mReviewRecyclerView.setAdapter(mReviewAdapter);
        mReviewRecyclerView.setLayoutManager(reviewLayoutManager);
        mReviewRecyclerView.addItemDecoration(reviewItemDecoration);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_movie_details);
        }

        if (getIntent() != null && getIntent().hasExtra("movie")) {

            final Movie movie = getIntent().getParcelableExtra("movie");

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

                mMovieReviewsUrl = NetworkUtils.getMovieReviewUrl(movie.getId());

                if (mMovieReviewsUrl != null) {
                    new FetchMovieReviewsTask(this, mReviewAdapter).execute(mMovieReviewsUrl);
                }

                mViewModel.getMovie(movie.getId()).observe(this, new Observer<Movie>() {
                    @Override
                    public void onChanged(@Nullable Movie movie) {
                        isMovieFavorite = (movie != null);
                        toggleFavoriteButton();
                    }
                });

                mFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!isMovieFavorite) {
                            mViewModel.insert(movie);
                        } else {
                            mViewModel.delete(movie.getId());
                        }

                        isMovieFavorite = !isMovieFavorite;
                        toggleFavoriteButton();
                    }
                });

            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.action_share_movie) {

            String shareString = getString(R.string.share_movie_message);

            String sharedMessage = String.format("%s : %s (%s)" ,
                    shareString, mTitle.getText().toString(), mReleaseDate.getText().toString());

            ShareCompat.IntentBuilder.from(this)
                    .setChooserTitle(R.string.share_title)
                    .setType("text/plain")
                    .setText(sharedMessage)
                    .startChooser();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTrailerClick(MovieTrailer trailer) {
        Uri playTrailerUri = NetworkUtils.getYoutubeUriFromVideoKey(trailer.getUrl());

        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, playTrailerUri);

        // Checks if the device has YouTube app for video playback
        if (trailerIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(trailerIntent);
        } else {

            // If not, the device will open a WebView for opening the video URL
            playTrailerUri = NetworkUtils.getWebUriFromVideoKey(trailer.getUrl());
            trailerIntent = new Intent(Intent.ACTION_VIEW, playTrailerUri);

            if (trailerIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(trailerIntent);
            } else {
                Toast.makeText(this, R.string.no_video_playback, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void toggleFavoriteButton() {

        if (isMovieFavorite) {
            mFavorite.setBackground(getResources().getDrawable(R.drawable.border_solid));
            Drawable favoriteIconSolid = getResources().getDrawable(R.drawable.ic_favorite_solid_24dp);
            favoriteIconSolid.setBounds(0, 0, 60, 60);
            mFavorite.setCompoundDrawables(favoriteIconSolid, null, null, null);
            mFavorite.setTextColor(getResources().getColor(R.color.colorWhite));
            mFavorite.setText(R.string.movie_added_favorites);
        } else {
            mFavorite.setBackground(getResources().getDrawable(R.drawable.border_outline));
            Drawable favoriteIconOutlined = getResources().getDrawable(R.drawable.ic_favorite_outline_24dp);
            favoriteIconOutlined.setBounds(0, 0, 60, 60);
            mFavorite.setCompoundDrawables(favoriteIconOutlined, null, null, null);
            mFavorite.setTextColor(getResources().getColor(R.color.colorAccent));
            mFavorite.setText(R.string.mark_movie_favorite);
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

            TextView noTrailerAvailable = activity.findViewById(R.id.tv_no_trailers);

            if (movieTrailers == null || movieTrailers.isEmpty()) {
                noTrailerAvailable.setVisibility(View.VISIBLE);
            } else {
                noTrailerAvailable.setVisibility(View.INVISIBLE);
            }

            if (movieTrailers != null) {
                trailerAdapter.setTrailers(movieTrailers);
            }
        }
    }

    private static class FetchMovieReviewsTask extends AsyncTask<URL, Void, List<MovieReview>> {

        private WeakReference<MovieDetailActivity> activityReference;

        private ReviewAdapter reviewAdapter;

        FetchMovieReviewsTask(MovieDetailActivity context, ReviewAdapter adapter) {
            activityReference = new WeakReference<>(context);
            reviewAdapter = adapter;
        }

        @Override
        protected List<MovieReview> doInBackground(URL... urls) {

            URL reviewUrl = urls[0];

            try {

                String jsonMovieReviewsResponse = NetworkUtils.getResponseFromHttpUrl(reviewUrl);

                return MovieJsonUtils.getMovieReviewsFromJson(jsonMovieReviewsResponse);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Cannot get response from movie reviews url");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Cannot parse the movie reviews JSON response");
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<MovieReview> movieReviews) {

            MovieDetailActivity activity = activityReference.get();

            if (activity == null || activity.isFinishing()) return;

            TextView noReviewAvailable = activity.findViewById(R.id.tv_no_reviews);

            if (movieReviews == null || movieReviews.isEmpty()) {
                noReviewAvailable.setVisibility(View.VISIBLE);
            } else {
                noReviewAvailable.setVisibility(View.INVISIBLE);
            }

            if (movieReviews != null) {
                reviewAdapter.setReviews(movieReviews);
            }
        }
    }
}
