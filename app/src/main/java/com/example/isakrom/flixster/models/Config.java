package com.example.isakrom.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by isakrom on 6/22/17.
 */

public class Config {
    String imagebaseURL;
    String postersize;
    //backdrop size for landscape
    String backdropSize;

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        imagebaseURL = images.getString("secure_base_url");
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        postersize = posterSizeOptions.optString(3, "w342");
        //landscape backdrop
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");
    }

    public String getImageURL(String size, String path) {
        return String.format("%s%s%s", imagebaseURL, size, path);
    }

    public String getImagebaseURL() {
        return imagebaseURL;
    }

    public String getPostersize() {
        return postersize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
