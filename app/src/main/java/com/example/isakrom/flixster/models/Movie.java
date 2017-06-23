package com.example.isakrom.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by isakrom on 6/22/17.
 */
@Parcel
public class Movie {
    //API values
    String title;
    String overview;
    String posterpath; // not full url
    String backdropPath;
    Double voteAverage;
    int runtime;
    //Constructor
    public Movie(){}
    //initialize form JSON object
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterpath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
        runtime = object.getInt("vote_count");
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterpath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}
