package com.example.android.movies.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.movies.MainActivity;
import com.example.android.movies.R;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XXX on 09-Aug-18.
 */

public class DetailsFragment extends Fragment {

    @BindView(R.id.movie_overview)
    TextView overviewView;

    @BindView(R.id.movie_title)
    TextView titleView;

    @BindView(R.id.smile_rating)
    SmileRating smiley_Rating;

    @BindView(R.id.release_date)
    TextView releaseDateView;

    @BindView(R.id.movie_poster)
    ImageView posterView;

    @BindView(R.id.rating_text)
    TextView ratingText;

    public DetailsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        String name = bundle.getString(MainActivity.TITLE);
        String image = bundle.getString(MainActivity.IMAGE);
        String overview = bundle.getString(MainActivity.OVERVIEW);
        String releaseDate = bundle.getString(MainActivity.RELEASE);
        double vote = bundle.getDouble(MainActivity.VOTE_AVERAGE);

        View view = inflater.inflate(R.layout.activity_details_fragment, container, false);

        ButterKnife.bind(this, view);

        // Title
        titleView.setText(name);

        // Image
        Picasso.get().load("http://image.tmdb.org/t/p/w500/" + image).
                error(R.drawable.no_poster).
                into(posterView);


        // Overview
        overviewView.setText(overview);
        overviewView.setMovementMethod(new ScrollingMovementMethod());

        // Release Date
        releaseDate = parseDate(releaseDate);
        releaseDateView.setText(getString(R.string.release_date) + releaseDate);

        // Vote Average
        setSmileyRating(vote);
        ratingText.setText(vote + "/10");

        registerForContextMenu(view);

        return view;
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

    private void setSmileyRating(double rating) {

        if (rating == 0) {

            smiley_Rating.setSelectedSmile(BaseRating.NONE, false);
            return;
        }

        double voteRating = rating / 2;

        if (voteRating < 1) {

            smiley_Rating.setSelectedSmile(BaseRating.TERRIBLE, false);
        } else if (voteRating < 2) {

            smiley_Rating.setSelectedSmile(BaseRating.BAD, false);
        } else if (voteRating < 3) {

            smiley_Rating.setSelectedSmile(BaseRating.OKAY, false);
        } else if (voteRating < 4) {

            smiley_Rating.setSelectedSmile(BaseRating.GOOD, false);
        } else {

            smiley_Rating.setSelectedSmile(BaseRating.GREAT, false);
        }


    }

}

