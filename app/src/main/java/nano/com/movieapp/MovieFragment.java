package nano.com.movieapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MovieFragment extends Fragment {
    private ImageAdapter mMovieAdapter;
    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridview = (GridView) rootView.findViewById(
                R.id.gridview);
        List<Movie> movies = new ArrayList<Movie>();
        mMovieAdapter = new ImageAdapter(getActivity(), movies);
        gridview.setAdapter(mMovieAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) mMovieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, movie);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sort_by_key),
                getString(R.string.pref_sort_by_default));
        movieTask.execute(sortOrder);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }


    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        public FetchMovieTask(){}

        @Override
        protected List<Movie> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;
            String sortOrder = "popularity.desc";
            //vote_average.desc
            try {
                final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
                final String SORT_BY = "sort_by";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, getString(R.string.api_key))
                        .appendQueryParameter(SORT_BY, params[0])
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // nothing to do
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    //nothing to do
                    return null;
                }
                movieJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieListFromJSON(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        private List<Movie> getMovieListFromJSON(String movieJSONStr)
                throws JSONException {
            List<Movie> movies = new ArrayList<Movie>();
            final String MOV_RESULTS = "results";
            final String MOV_TITLE = "original_title";
            final String MOV_IMAGE = "poster_path";
            final String MOV_OVERVIEW = "overview";
            final String MOV_USER_RATING = "vote_average";
            final String MOV_DATE = "release_date";

            JSONObject movieJSON = new JSONObject(movieJSONStr);
            JSONArray movieArray = movieJSON.getJSONArray(MOV_RESULTS);

            for (int i = 0; i< movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                movies.add(new Movie(
                        movie.getString(MOV_TITLE),
                        movie.getDouble(MOV_USER_RATING),
                        movie.getString(MOV_OVERVIEW),
                        movie.getString(MOV_DATE),
                        movie.getString(MOV_IMAGE)));
            }
            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            if (result != null) {
                mMovieAdapter.clear();
                mMovieAdapter.addAll(result);
            }
        }
    }
}



