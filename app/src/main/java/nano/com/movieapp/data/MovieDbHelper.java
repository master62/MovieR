package nano.com.movieapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import nano.com.movieapp.data.MovieContract.MovieCollection;
/**
 * Manages a local database for favourite movies
 */
public class MovieDbHelper extends SQLiteOpenHelper{


    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieCollection.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                MovieCollection._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the movie
                MovieCollection.COLUMN_ID + "INTEGER NOT NULL" +
                MovieCollection.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieCollection.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieCollection.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieCollection.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +

                MovieCollection.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieCollection.COLUMN_POPULARITY + " TEXT NOT NULL, " +

                MovieCollection.COLUMN_REVIEWS + " TEXT NOT NULL, " +
                MovieCollection.COLUMN_TRAILER + " TEXT NOT NULL, " ;





        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
