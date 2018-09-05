package com.example.android.movies.Data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Created by XXX on 05-Aug-18.
 */

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository mRepository;
    private LiveData<List<FavoriteMovie>> movies;

    public MovieViewModel(Application application) {

        super(application);

        FavoriteRoomDatabase favoriteRoomDatabase = FavoriteRoomDatabase.getDatabase(this.getApplication());
        mRepository = new MovieRepository(application);
        movies = favoriteRoomDatabase.movieDao().getAllMovies();
    }

    public LiveData<List<FavoriteMovie>> getAllMovies() {
        return movies;
    }

    public void insert(FavoriteMovie favoriteMovie) {

        mRepository.insert(favoriteMovie);
    }

}
