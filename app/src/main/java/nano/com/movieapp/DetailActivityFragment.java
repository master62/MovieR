package nano.com.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    Movie movies;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        if (intent != null && intent.hasExtra(intent.EXTRA_TEXT)) {
            movies = (Movie) intent.getSerializableExtra(intent.EXTRA_TEXT);

            ((TextView) rootView.findViewById(R.id.detail_title)).
                    setText(movies.getTitle());

            //  plot
            ((TextView) rootView.findViewById(R.id.detail_date)).setText(movies.getOverview());

            //  user rating
            double rating = movies.getUserRating();
            ((TextView) rootView.findViewById(R.id.detail_userRating)).setText(String.valueOf(rating) + "/10");

            //  release date
            String date = movies.getReleaseDate();
            String year = date.substring(0, 4);
            ((TextView) rootView.findViewById(R.id.detail_date)).setText(year);

            //  icon
            StringBuilder sb = new StringBuilder();
            sb.append(getActivity().getString(R.string.icon_path));
            sb.append(movies.getPosterPath());
            ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_icon);
            Picasso.with(getActivity()).load(sb.toString()).into(imageView);

            //  synopsis
            ((TextView) rootView.findViewById(R.id.detail_plot)).setText(movies.getOverview());

        }
        return rootView;
    }
}