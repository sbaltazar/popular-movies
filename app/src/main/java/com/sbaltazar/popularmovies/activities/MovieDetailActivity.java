package com.sbaltazar.popularmovies.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.sbaltazar.popularmovies.R;
import com.sbaltazar.popularmovies.models.Movie;

import java.util.Calendar;
import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mRating;
    private TextView mSynopsis;
    private ImageView mPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitle = findViewById(R.id.tv_movie_title);
        mReleaseDate = findViewById(R.id.tv_movie_release_date);
        mRating = findViewById(R.id.tv_movie_rating);
        mSynopsis = findViewById(R.id.tv_movie_synopsis);
        mPoster = findViewById(R.id.iv_movie_poster);


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
            } else {
                finish();
            }
        }
    }
}
