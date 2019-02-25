package com.sbaltazar.popularmovies.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sbaltazar.popularmovies.R;
import com.sbaltazar.popularmovies.adapters.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

public class MovieDiscoveryActivity extends AppCompatActivity {

    private RecyclerView mMovieRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_discovery);

        mMovieRecyclerView = findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        mMovieRecyclerView.setLayoutManager(layoutManager);

        List<String> titles = new ArrayList<>();

        titles.add("La coca");
        titles.add("Stan Lord");
        titles.add("The parody");
        titles.add("Washaka King");
        titles.add("The Return");
        titles.add("Mendozas Summer's Surprise");
        titles.add("La chocaviga");
        titles.add("Sezaline");
        titles.add("Migration stopping");

        MovieAdapter adapter = new MovieAdapter(titles);

        mMovieRecyclerView.setAdapter(adapter);
    }
}
