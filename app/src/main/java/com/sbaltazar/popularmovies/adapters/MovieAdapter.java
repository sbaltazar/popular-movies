package com.sbaltazar.popularmovies.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sbaltazar.popularmovies.R;
import com.sbaltazar.popularmovies.activities.MovieDiscoveryActivity;
import com.sbaltazar.popularmovies.models.Movie;
import com.sbaltazar.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> mMovies;

    final private MoviePosterClickListener mOnClickListener;

    private int mWidth;

    public interface MoviePosterClickListener {
        void onMoviePosterClick(String title);
    }

    public MovieAdapter(MoviePosterClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_movie_tile, viewGroup, false);

        mWidth = viewGroup.getMeasuredWidth() / MovieDiscoveryActivity.NUMBER_OF_COLUMNS;

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        movieViewHolder.bind();
    }

    @Override
    public int getItemCount() {
        if (mMovies != null) {
            return mMovies.size();
        }
        return 0;
    }

    public void setMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView poster;
        private TextView title;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.iv_movie_poster);

            itemView.setOnClickListener(this);
        }

        void bind() {
            int position = getAdapterPosition();

            // Getting the image bitmap to get the correct view size
            Bitmap movieImage = mMovies.get(position).getImage();

            double scale = (double) mWidth / movieImage.getWidth();

            double minHeight = movieImage.getHeight() * scale;

            // Setting ImageView dimensions and image
            poster.setMinimumWidth(mWidth);
            poster.setMinimumHeight((int) Math.round(minHeight));
            poster.setImageBitmap(movieImage);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String title = mMovies.get(position).getTitle();
            mOnClickListener.onMoviePosterClick(title);
        }
    }
}
