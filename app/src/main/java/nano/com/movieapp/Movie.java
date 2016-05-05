package nano.com.movieapp;
import java.io.Serializable;

/**
 * Created by Home on 1/11/2016.
 */
public class Movie implements Serializable {
    private static final String LOG_TAG = Movie.class.getSimpleName();
    private String title;
    private double userRating;
    private String overview;
    private String releaseDate;
    private String posterPath;


    public Movie(String title, double userRating, String overview, String releaseDate, String posterPath) {
        this.title = title;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getUserRating() {
        return userRating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }
}

