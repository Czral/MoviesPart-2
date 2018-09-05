package com.example.android.movies.Data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by XXX on 05-Aug-18.
 */

public class MovieRepository {

    private MovieDao mMovieDao;
    private LiveData<List<FavoriteMovie>> mAllMovies;

    MovieRepository(Application application) {

        FavoriteRoomDatabase db = FavoriteRoomDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        mAllMovies = mMovieDao.getAllMovies();
    }

    public void insert(FavoriteMovie favoriteMovie) {

        new insertAsyncTask(mMovieDao).execute(favoriteMovie);
    }

    private static class insertAsyncTask extends AsyncTask<FavoriteMovie, Void, Void> {

        private MovieDao asyncMovieDao;

        insertAsyncTask(MovieDao movieDao) {

            asyncMovieDao = movieDao;
        }

        @Override
        protected Void doInBackground(FavoriteMovie... favoriteMovies) {
            asyncMovieDao.insert(favoriteMovies[0]);
            return null;
        }
    }



}

