package nano.com.movieapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 *
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final int MOVIE = 1;
    static final int MOVIES_WITH_ID = 2;


    static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE, MOVIE);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/#", MOVIES_WITH_ID);

        return uriMatcher;
    }

    public boolean onCreate(){

        mOpenHelper = new MovieDbHelper(getContext());

        return true;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case MOVIE:{
                cursor = db.query(MovieContract.PATH_MOVIE,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            }
            default:
                throw new IllegalArgumentException("Unsupported Uri" + uri);
        }
        if(getContext()!=null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri){

       final int id = sUriMatcher.match(uri);

        switch (id){

            case MOVIE:
                return MovieContract.MovieCollection.CONTENT_TYPE;
            case MOVIES_WITH_ID:
                return MovieContract.MovieCollection.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknow URI" + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri uri1=null;
        switch (sUriMatcher.match(uri)){
            case MOVIE:{
                long id = db.insert(MovieContract.PATH_MOVIE, null, values);
                if(id!=-1){
                    uri1 = MovieContract.getUriForId(id);
                }
                break;
            }
            default:{
                throw new IllegalArgumentException("Unsupported Uri For Insertion " + uri);
            }
        }
        if(getContext()!=null)
            getContext().getContentResolver().notifyChange(uri, null);
        return uri1;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int delete;
        switch (sUriMatcher.match(uri)){
            case MOVIES_WITH_ID:{
                delete = db.delete(MovieContract.PATH_MOVIE, selection, selectionArgs);
                break;
            }
            case MOVIE:{
                delete = db.delete(MovieContract.PATH_MOVIE, selection, selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Unsupported Uri For Deletion " + uri);
        }
        if(getContext()!=null&&delete!=0)
            getContext().getContentResolver().notifyChange(uri, null);
        return delete;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int update;
        switch (sUriMatcher.match(uri)){
            case MOVIES_WITH_ID:{
                update = db.update(MovieContract.PATH_MOVIE, values, selection, selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Unsupported Uri For Updating " + uri);
        }
        if(getContext()!=null)
            getContext().getContentResolver().notifyChange(uri, null);
        return update;
    }
}

