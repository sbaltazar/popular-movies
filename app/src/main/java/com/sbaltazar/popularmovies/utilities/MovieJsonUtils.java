package com.sbaltazar.popularmovies.utilities;

import android.graphics.Bitmap;

import com.sbaltazar.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class MovieJsonUtils {

    public static List<Movie> getMoviesFromJson(String json) throws JSONException, IOException {

        final String KEY_RESULTS = "results";
        final String KEY_TITLE = "title";
        final String KEY_POSTER_PATH = "poster_path";
        // TODO: Add overview

        List<Movie> movieList = new ArrayList<>();

        JSONObject moviesJson = new JSONObject(json);

//        // If the response has movies to add to the list
//        if(moviesJson.has(KEY_RESULTS)){
//
//        }

        JSONArray movieArray = moviesJson.getJSONArray(KEY_RESULTS);

        for(int i = 0; i < movieArray.length(); i++){

            JSONObject movieJsonObject = movieArray.getJSONObject(i);

            String title = movieJsonObject.getString(KEY_TITLE);
            String imagePath = NetworkUtils.IMAGE_SIZE_PATH + movieJsonObject.getString(KEY_POSTER_PATH);
            Bitmap image = Picasso.get().load(imagePath).get();

            movieList.add(new Movie(title, imagePath, image));
        }

        return movieList;
    }
}
