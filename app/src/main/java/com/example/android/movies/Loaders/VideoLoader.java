package com.example.android.movies.Loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.android.movies.Data.MovieUtils;
import com.example.android.movies.Files.Video;

import java.util.List;

/**
 * Created by XXX on 04-Aug-18.
 */

public class VideoLoader extends AsyncTaskLoader<List<Video>> {

    private String mUrl;
    private String TAG = VideoLoader.class.getName();

    public VideoLoader(Context context, String url) {

        super(context);
        mUrl = url;

    }

    @Override
    public List<Video> loadInBackground() {

        if (mUrl == null) {

            Log.d(TAG, "URL empty");
            return null;
        } else {

            List<Video> videos = MovieUtils.fetchVideos(mUrl);
            return videos;
        }

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


}


