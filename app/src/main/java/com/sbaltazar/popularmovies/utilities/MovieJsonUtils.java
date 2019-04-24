package com.sbaltazar.popularmovies.utilities;

import android.graphics.Bitmap;

import com.sbaltazar.popularmovies.models.Movie;
import com.sbaltazar.popularmovies.models.MovieTrailer;
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

        final String KEY_ID = "id";
        final String KEY_RESULTS = "results";
        final String KEY_TITLE = "title";
        final String KEY_POSTER_PATH = "poster_path";
        final String KEY_SYNOPSIS = "overview";
        final String KEY_RELEASE_DATE = "release_date";
        final String KEY_VOTE_AVG = "vote_average";

        List<Movie> movieList = new ArrayList<>();

        JSONObject moviesJson = new JSONObject(json);

        JSONArray movieArray = moviesJson.getJSONArray(KEY_RESULTS);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieJsonObject = movieArray.getJSONObject(i);

            int id = movieJsonObject.getInt(KEY_ID);
            String title = movieJsonObject.getString(KEY_TITLE);
            String imagePath = NetworkUtils.IMAGE_SIZE_PATH + movieJsonObject.getString(KEY_POSTER_PATH);
            Bitmap image = Picasso.get().load(imagePath).get();
            String synopsis = movieJsonObject.getString(KEY_SYNOPSIS);
            Date releaseDate = sdf.parse(movieJsonObject.getString(KEY_RELEASE_DATE));
            double voteAvg = movieJsonObject.getDouble(KEY_VOTE_AVG);

            movieList.add(new Movie(id, title, imagePath, image, releaseDate, voteAvg, synopsis));
        }

        return movieList;
    }

    public static List<MovieTrailer> getMovieTrailersFromJson(String json) throws JSONException {

        // Movie ID
        final String KEY_MOVIE_ID = "id";
        // Trailer details
        final String KEY_ID = "id";
        final String KEY_RESULTS = "results";
        final String KEY_NAME = "name";
        final String KEY_VIDEO_KEY = "key";

        List<MovieTrailer> trailerList = new ArrayList<>();

        JSONObject trailersJson = new JSONObject(json);

        int movieId = trailersJson.getInt(KEY_MOVIE_ID);

        JSONArray trailerArray = trailersJson.getJSONArray(KEY_RESULTS);

        for (int i = 0; i < trailerArray.length(); i++) {

            JSONObject trailerJsonObject = trailerArray.getJSONObject(i);

            String id = trailerJsonObject.getString(KEY_ID);
            String title = trailerJsonObject.getString(KEY_NAME);
            String videoKey = trailerJsonObject.getString(KEY_VIDEO_KEY);

            trailerList.add(new MovieTrailer(id, title, movieId, videoKey));
        }

        return trailerList;
    }
}
