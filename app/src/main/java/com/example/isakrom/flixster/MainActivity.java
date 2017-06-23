package com.example.isakrom.flixster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.isakrom.flixster.models.Config;
import com.example.isakrom.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    //Basic data for movie info request
    public final static String API_BASE_URL ="https://api.themoviedb.org/3";
    public final static String API_KEY_PARAM = "api_key";
    //Creates client
    AsyncHttpClient client;
    public final static String TAG = "MovieListActivity";
    //base url for poster image
    String imageBaseURL;
    //poster size for image fetching
    String posterSize;
    //list of movies
    ArrayList<Movie> movies;
    RecyclerView rvMovies;
    //adapter to recdycler view
    MovieAdapter adapter;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //starts a client
        client = new AsyncHttpClient();
        // create movie list
        movies = new ArrayList<>();
        //initialize movies array list
        adapter = new MovieAdapter(movies);
        //resolve recycle view
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);
        // You have to get config as the app starts
        getConfiguration();
    }
    //get list of movies
    private void getNowPlaying(){
        String url = API_BASE_URL + "/movie/now_playing";
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        client.get(url, params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load movies into list
                try {
                    JSONArray results = response.getJSONArray("results");
                    // iterate through results and add each
                    for (int i = 0; i < results.length(); i++){
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get the data from now playing endpoint", throwable, true);
            }
        });
    }
    private void getConfiguration() {
        //Sets up the url params that are supposed to be constant
        String url = API_BASE_URL + "/configuration";
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        //reaches out to tmdb to get info for URL and has built in error function
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with ImageBaseURL %s and posterSize %s", config.getImagebaseURL(), config.getPostersize()));
                    //Yo check out the section on setting up the image base URL function and the network error

                    // get now playnig movie list
                    adapter.setConfig(config);
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration.", throwable, true);
            }
        });

    }

    private void logError(String message, Throwable error, boolean alertUser) {
        Log.e(TAG, message, error);
        if (alertUser) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
