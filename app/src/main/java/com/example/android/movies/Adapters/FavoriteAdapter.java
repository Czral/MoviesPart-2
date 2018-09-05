package com.example.android.movies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movies.Data.FavoriteMovie;
import com.example.android.movies.R;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by XXX on 06-Aug-18.
 */

public class FavoriteAdapter extends BaseAdapter {

    private Context mContext;
    private List<FavoriteMovie> favoriteMovies;

    public FavoriteAdapter(Context context) {

        mContext = context;
    }

    @Override
    public int getCount() {

        if (favoriteMovies == null) {
            return 0;
        }
        return favoriteMovies.size();
    }

    public List<FavoriteMovie> getFavoriteMovies() {

        return favoriteMovies;
    }

    @Override
    public Object getItem(int position) {
        return favoriteMovies.get(position);
    }

    public void setFavoriteMovies(List<FavoriteMovie> sFavoriteMovies) {

        favoriteMovies = sFavoriteMovies;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View favoriteView = convertView;

        if (favoriteView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            favoriteView = layoutInflater.inflate(R.layout.activity_favorite_list, null);
        }

        TextView title = favoriteView.findViewById(R.id.movie_title);
        ImageView image = favoriteView.findViewById(R.id.movie_poster);
        TextView overview = favoriteView.findViewById(R.id.movie_overview);
        TextView release = favoriteView.findViewById(R.id.release_date);
        SmileRating vote = favoriteView.findViewById(R.id.smile_rating);
        TextView voteText = favoriteView.findViewById(R.id.rating_text);

        FavoriteMovie favoriteMovie = favoriteMovies.get(position);

        String date = favoriteMovie.getDate();
        date = parseDate(date);

        double voteAverage = favoriteMovie.getVoteAverage();

        double voteRating = voteAverage / 2;

        if (voteRating < 1) {

            vote.setSelectedSmile(BaseRating.TERRIBLE, false);
        } else if (voteRating < 2) {

            vote.setSelectedSmile(BaseRating.BAD, false);
        } else if (voteRating < 3) {

            vote.setSelectedSmile(BaseRating.OKAY, false);
        } else if (voteRating < 4) {

            vote.setSelectedSmile(BaseRating.GOOD, false);
        } else {

            vote.setSelectedSmile(BaseRating.GREAT, false);
        }

        title.setText(favoriteMovie.getTitle());
        overview.setText(favoriteMovie.getOverview());
        release.setText(favoriteView.getResources().getString(R.string.release_date) + date);
        voteText.setText(voteAverage + "/10");

        if (favoriteMovie.getImage() == null) {
            image.setImageResource(R.drawable.no_poster);
        } else {
            Picasso.get().
                    load("http://image.tmdb.org/t/p/w500/" + favoriteMovie.getImage())
                    .error(R.drawable.no_poster).into(image);

        }
        return favoriteView;
    }


    private String parseDate(String dateString) {
        String input = "yyyy-MM-dd";
        String output = "dd-MMM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(input);
        SimpleDateFormat outputFormat = new SimpleDateFormat(output);
        Date date;
        String finalDate = null;
        try {
            date = dateFormat.parse(dateString);
            finalDate = outputFormat.format(date);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return finalDate;
    }


}
