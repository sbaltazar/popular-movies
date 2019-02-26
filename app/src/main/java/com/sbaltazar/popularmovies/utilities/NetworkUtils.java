package com.sbaltazar.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.sbaltazar.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_DATABASE_ROOT_URL =
            "http://api.themoviedb.org/3/";

    private static final String SECURE_MOVIE_DATABASE_ROOT_URL =
            "https://api.themoviedb.org/3/";

    private static final String ROOT_IMAGE_URL = "https://image.tmdb.org/t/p/";


    // List of posible poster's sizes
    private static final String POSTER_SIZE_W92 = "w92";
    private static final String POSTER_SIZE_W154 = "w154";
    private static final String POSTER_SIZE_W185 = "w185";
    private static final String POSTER_SIZE_W342 = "w342";
    private static final String POSTER_SIZE_W500 = "w500";
    private static final String POSTER_SIZE_W780 = "w780";
    private static final String POSTER_SIZE_ORIGINAL = "original";

    // URL to get the image
    static final String IMAGE_SIZE_PATH = ROOT_IMAGE_URL + POSTER_SIZE_W185;

    private static final String MOVIE_PATH = "movie";
    private static final String POPULAR_MOVIE_PATH = MOVIE_PATH + "/popular";
    private static final String TOP_RATED_MOVIE_PATH = MOVIE_PATH + "/top_rated";

    private static final String QUERY_API_KEY = "api_key";

    private static final String API_KEY = BuildConfig.API_KEY;

    public static URL getPopularMovieUrl() {
        Uri builtUri = Uri.parse(MOVIE_DATABASE_ROOT_URL + POPULAR_MOVIE_PATH).buildUpon()
                .appendQueryParameter(QUERY_API_KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Popular movie URL malformed");
        }

        return url;
    }

    public static URL getTopRatedMovieUrl() {
        Uri builtUri = Uri.parse(MOVIE_DATABASE_ROOT_URL + TOP_RATED_MOVIE_PATH).buildUpon()
                .appendQueryParameter(QUERY_API_KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Top rated movie URL malformed");
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
