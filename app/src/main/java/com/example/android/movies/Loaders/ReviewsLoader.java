package com.example.android.movies.Loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.android.movies.Data.MovieUtils;
import com.example.android.movies.Files.Reviews;

import java.util.List;

/**
 * Created by XXX on 04-Aug-18.
 */

public class ReviewsLoader extends AsyncTaskLoader<List<Reviews>> {

    private String mUrl;
    private String TAG = ReviewsLoader.class.getName();

    public ReviewsLoader(Context context, String url) {

        super(context);
        mUrl = url;

    }

    @Override
    public List<Reviews> loadInBackground() {

        if (mUrl == null) {

            Log.d(TAG, "URL empty");
            return null;
        } else {

            List<Reviews> reviews = MovieUtils.fetchReviews(mUrl);
            return reviews;
        }
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
