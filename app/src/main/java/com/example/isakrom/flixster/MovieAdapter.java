package com.example.isakrom.flixster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.isakrom.flixster.models.Config;
import com.example.isakrom.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by isakrom on 6/22/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //List of Movies
    ArrayList<Movie> movies;
    //Config needed for image url
    Config config;
    //Context for render
    Context context;
    // initialize with list

    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    // creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //get context from paretns
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }
    //associates an inflated view for an item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get movie datat at position
        Movie movie = movies.get(position);
        //populate view with movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());
        //determine orientation
        boolean isPortait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        //build url for poster image
        String imageURL = null;
        //if in portrait mode, load poster image
        if (isPortait) {
            imageURL = config.getImageURL(config.getPostersize(), movie.getPosterPath());
        } else {
            //load backdrop
            imageURL = config.getImageURL(config.getBackdropSize(), movie.getBackdropPath());
        }
        //get correct placeholder
        int placeholderID = isPortait ? R.drawable.flicks : R.drawable.flicks_backdrop;
        ImageView imageView = isPortait ? holder.ivPosterImage : holder.ivBackdropImage;
        //Set image
        Glide.with(context)
                .load(imageURL)
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                .placeholder(placeholderID)
                .error(placeholderID)
                .into(imageView);
    }
    // returns total number of movies
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // create the viewholder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        //  declare fields , in this case view items
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;


        public ViewHolder(View itemView) {
            super(itemView);
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdrop);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            // add this as the itemView's OnClickListener
            itemView.setOnClickListener(this);
        }

        // when the user clicks on a row, show MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
