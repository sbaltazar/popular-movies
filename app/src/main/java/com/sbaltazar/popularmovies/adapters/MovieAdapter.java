package com.sbaltazar.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sbaltazar.popularmovies.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<String> mMovieTitles;

    public MovieAdapter(List<String> movieTitles) {
        mMovieTitles = movieTitles;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_movie_tile, viewGroup, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        movieViewHolder.bind();
    }

    @Override
    public int getItemCount() {
        if(mMovieTitles != null){
            return mMovieTitles.size();
        }
        return 0;
    }

    public void setMovieTitles(List<String> titles){
        mMovieTitles = titles;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView poster;
        private TextView title;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.iv_movie_poster);
            title = itemView.findViewById(R.id.tv_movie_title);
        }

        void bind(){
            int position = getAdapterPosition();
            title.setText(mMovieTitles.get(position));
        }

    }
}
