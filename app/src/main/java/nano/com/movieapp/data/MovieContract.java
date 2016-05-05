package nano.com.movieapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Movie DB Structure
 */
public class MovieContract {


    public static final String CONTENT_AUTHORITY = "nano.com.movieapp";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Same as database name
    public static final String PATH_MOVIE = "movie";



    public static final class MovieCollection implements BaseColumns{


        public static final Uri CONTENT_URI =  BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                               ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                              ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;


        public static final String TABLE_NAME = "movie";

        public final static String COLUMN_ID = "id";

        public final static String COLUMN_BACKDROP_PATH = "backdrop";

        public final static String COLUMN_POSTER_PATH = "poster";

        public final static String COLUMN_TITLE = "title";

        public final static String COLUMN_RELEASE_DATE = "release_date";

        public final static String COLUMN_OVERVIEW = "overview";

        public final static String COLUMN_POPULARITY = "popularity";

        public final static String COLUMN_REVIEWS = "reviews";

        public final static String COLUMN_TRAILER = "trailer";




    }

    public static final class FavouriteMovies implements BaseColumns{

        public final static String TABLE_NAME = "favourite";


        public final static String COLUMN_ID = "id";


    }


    public static Uri getUriForId(long id){
        return MovieCollection.CONTENT_URI.buildUpon().appendPath(Integer.toString((int)id)).build();
    }
}
