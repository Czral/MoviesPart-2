package com.example.android.movies.Loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.android.movies.Data.MovieUtils;
import com.example.android.movies.Files.MovieFile;

import java.util.List;

/**
 * Created by XXX on 04-Aug-18.
 */

public class MoviesLoader extends AsyncTaskLoader<List<MovieFile>> {

    private String mUrl;
    private String TAG = MoviesLoader.class.getName();

    public MoviesLoader(Context context, String url) {

        super(context);
        mUrl = url;
    }

    @Override
    public List<MovieFile> loadInBackground() {

        if (mUrl == null) {

            Log.d(TAG, "URL empty");
            return null;
        } else {

            List<MovieFile> movies = MovieUtils.fetchMovieData(mUrl);
            return movies;
        }

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
