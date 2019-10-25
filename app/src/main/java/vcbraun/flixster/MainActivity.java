package vcbraun.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import vcbraun.flixster.adapters.MovieAdapter;
import vcbraun.flixster.models.Movie;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing";
    public static final String CONFIGURATION_URL = "https://api.themoviedb.org/3/configuration";
    public static final int POSTER_SIZE = 3;    //index of the image size to use for the poster
    public static final int BACKDROP_SIZE = 3;  //index for the image size to use for the backdrop

    String imageBaseURL;
    List<String> posterSizes = new ArrayList<>();
    List<String> backdropSizes = new ArrayList<>();
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movies = new ArrayList<>();

        RecyclerView rvMovies = findViewById(R.id.rvMovies);

        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        rvMovies.setAdapter(movieAdapter);

        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String secretValue = getString(R.string.tmdb_api_key);
        params.put("api_key", "a07e22bc18f5cb106bfe4cc1f83ad8ed");

        // image formats request
        client.get(CONFIGURATION_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject configObject = json.jsonObject;

                try {
                    JSONObject imageData = configObject.getJSONObject("images");
                    imageBaseURL = imageData.getString("secure_base_url");

                    JSONArray posterSizesArray = imageData.getJSONArray("poster_sizes");
                    for(int i = 0; i < posterSizesArray.length(); i++) {
                        posterSizes.add(posterSizesArray.getString(i));
                    }

                    JSONArray backdropSizesArray = imageData.getJSONArray("backdrop_sizes");
                    for(int i = 0; i < posterSizesArray.length(); i++) {
                        backdropSizes.add(posterSizesArray.getString(i));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        // movie information request
        client.get(NOW_PLAYING_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject moviesObject = json.jsonObject;
                try {
                    JSONArray movieResults = moviesObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + movieResults.toString());

                    movies.addAll(Movie.fromJSONArray(movieResults,
                                                      imageBaseURL,
                                                      posterSizes.get(POSTER_SIZE),
                                                      backdropSizes.get(BACKDROP_SIZE)));

                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "movies: " + movies.size());
                } catch (JSONException e) {
                    Log.d(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}
