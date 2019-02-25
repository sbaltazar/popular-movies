package com.sbaltazar.popularmovies.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.sbaltazar.popularmovies.R;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (getIntent() != null) {
            if (getIntent().hasExtra(Intent.EXTRA_TEXT)) {

                String movieTitle = getIntent().getStringExtra(Intent.EXTRA_TEXT);

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(movieTitle);
                } else{
                    Log.e(TAG,"No support action bar available");
                }
            }
        }
    }
}
