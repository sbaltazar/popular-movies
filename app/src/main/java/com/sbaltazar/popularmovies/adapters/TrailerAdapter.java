package com.sbaltazar.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sbaltazar.popularmovies.R;
import com.sbaltazar.popularmovies.models.MovieTrailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private List<MovieTrailer> mMovieTrailers;

    final private TrailerClickListener mOnClickListener;

    public interface TrailerClickListener {
        void onTrailerClick(MovieTrailer trailer);
    }

    public TrailerAdapter(TrailerClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_movie_trailer, viewGroup, false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder viewHolder, int i) {
        viewHolder.bind();
    }

    @Override
    public int getItemCount() {
        if (mMovieTrailers != null) {
            return mMovieTrailers.size();
        }
        return 0;
    }

    public void setTrailers(List<MovieTrailer> trailers) {
        mMovieTrailers = trailers;
        notifyDataSetChanged();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final private TextView title;

        TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_trailer_title);

            itemView.setOnClickListener(this);
        }

        void bind() {
            int position = getAdapterPosition();
            title.setText(mMovieTrailers.get(position).getTitle());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onTrailerClick(mMovieTrailers.get(position));
        }
    }

}
