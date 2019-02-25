package com.sbaltazar.popularmovies.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.sbaltazar.popularmovies.R;
import com.sbaltazar.popularmovies.adapters.MovieAdapter;
import com.sbaltazar.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDiscoveryActivity extends AppCompatActivity implements MovieAdapter.MoviePosterClickListener {

    private RecyclerView mMovieRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_discovery);

        mMovieRecyclerView = findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        mMovieRecyclerView.setLayoutManager(layoutManager);

        List<Movie> titles = new ArrayList<>();

        titles.add(new Movie("La coca", null));
        titles.add(new Movie("Stan Lord", null));
        titles.add(new Movie("The parody", null));
        titles.add(new Movie("Washaka King", null));
        titles.add(new Movie("The Return", null));
        titles.add(new Movie("Mendozas Summer's Surprise", null));
        titles.add(new Movie("La chocaviga", null));
        titles.add(new Movie("Sezaline", null));
        titles.add(new Movie("Migration stopping", null));

        MovieAdapter adapter = new MovieAdapter(titles, this);

        mMovieRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onMoviePosterClick(String title) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, title);
        startActivity(intent);
    }
}
