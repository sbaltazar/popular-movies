package com.sbaltazar.popularmovies.utilities;

import android.graphics.Bitmap;

import com.sbaltazar.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class MovieJsonUtils {

    public static List<Movie> getMoviesFromJson(String json) throws JSONException, IOException, ParseException {

        final String KEY_RESULTS = "results";
        final String KEY_TITLE = "title";
        final String KEY_POSTER_PATH = "poster_path";
        final String KEY_SYNOPSIS = "overview";
        final String KEY_RELEASE_DATE = "release_date";
        final String KEY_VOTE_AVG = "vote_average";

        List<Movie> movieList = new ArrayList<>();

        JSONObject moviesJson = new JSONObject(json);

//        // If the response has movies to add to the list
//        if(moviesJson.has(KEY_RESULTS)){
//
//        }

        JSONArray movieArray = moviesJson.getJSONArray(KEY_RESULTS);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


        for (int i = 0; i < movieArray.length(); i++) {


            JSONObject movieJsonObject = movieArray.getJSONObject(i);

            String title = movieJsonObject.getString(KEY_TITLE);
            String imagePath = NetworkUtils.IMAGE_SIZE_PATH + movieJsonObject.getString(KEY_POSTER_PATH);
            Bitmap image = Picasso.get().load(imagePath).get();
            String synopsis = movieJsonObject.getString(KEY_SYNOPSIS);
            Date releaseDate = sdf.parse(movieJsonObject.getString(KEY_RELEASE_DATE));
            double voteAvg = movieJsonObject.getDouble(KEY_VOTE_AVG);

            movieList.add(new Movie(title, imagePath, image, releaseDate, voteAvg, synopsis));
        }

        return movieList;
    }
}
