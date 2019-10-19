package vcbraun.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String imageURL;
    private String posterSize;
    private String posterPath;
    private String backdropSize;
    private String backdropPath;
    private String title;
    private String overview;

    public Movie(JSONObject jsonObject, String imageURL, String posterSize, String backdropSize) throws JSONException {
        this.imageURL = imageURL;
        this.posterSize = posterSize;
        this.backdropSize = backdropSize;

        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
    }

    public static List<Movie> fromJSONArray(JSONArray movieJsonArray, String imgURL, String posterSize, String backdropSize) throws JSONException{
        List<Movie> movies = new ArrayList<>();

        for(int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i), imgURL, posterSize, backdropSize));
        }
        return movies;
    }

    public String getBackdropPath() {
        return String.format("%s%s%s", imageURL, backdropSize, backdropPath);
    }

    public String getPosterPath() {
        return String.format("%s%s%s", imageURL, posterSize, posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }
}
