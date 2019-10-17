package vcbraun.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Headers;
import vcbraun.flixster.models.Movie;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing";
    public static final String CONFIGURATION_URL = "https://api.themoviedb.org/3/configuration";
    public static final String TAG = "MainActivity";

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("api_key", "a07e22bc18f5cb106bfe4cc1f83ad8ed");

        // image formats request
        client.get(CONFIGURATION_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject configObject = json.jsonObject;

                try {
                    JSONObject imageData = configObject.getJSONObject("poster_sizes");
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

                    movies = Movie.fromJSONArray(movieResults);

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
