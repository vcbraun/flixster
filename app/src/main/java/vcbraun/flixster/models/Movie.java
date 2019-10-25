package vcbraun.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {
    String imageURL;
    String posterSize;
    String posterPath;
    String backdropSize;
    String backdropPath;
    String title;
    String overview;

    double rating;
    int id;
    // needed by Parceler library
    public Movie() {}

    public Movie(JSONObject jsonObject, String imageURL, String posterSize, String backdropSize) throws JSONException {
        this.imageURL = imageURL;
        this.posterSize = posterSize;
        this.backdropSize = backdropSize;

        id = jsonObject.getInt("id");
        rating = jsonObject.getDouble("vote_average");
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

    public int getId() { return id; }

    public double getRating() { return rating; }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }
}
