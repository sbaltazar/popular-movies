package com.sbaltazar.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sbaltazar.popularmovies.R;
import com.sbaltazar.popularmovies.models.MovieReview;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private List<MovieReview> mMovieReviews;

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_movie_review, viewGroup, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder viewHolder, int i) {
        viewHolder.bind();
    }

    @Override
    public int getItemCount() {
        if (mMovieReviews != null) {
            return mMovieReviews.size();
        }
        return 0;
    }

    public void setReviews(List<MovieReview> reviews) {
        mMovieReviews = reviews;
        notifyDataSetChanged();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder  {

        final private TextView author;
        final private TextView review;
        final private Button readMore;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.tv_author_name);
            review = itemView.findViewById(R.id.tv_movie_review);
            readMore = itemView.findViewById(R.id.btn_read_review);
        }

        void bind() {
            int position = getAdapterPosition();

            author.setText(mMovieReviews.get(position).getAuthor());
            review.setText(mMovieReviews.get(position).getContent());

            // TODO: Add dialog to show full review
            //readMore.setOnClickListener();
        }

    }
}
