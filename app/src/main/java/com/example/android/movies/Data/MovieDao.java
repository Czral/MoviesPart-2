package com.example.android.movies.Data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

/**
 * Created by XXX on 05-Aug-18.
 */

@Dao
public interface MovieDao {

    @Insert
    void insert(FavoriteMovie favoriteMovie);

    @Query("SELECT * FROM favorites_table")
    LiveData<List<FavoriteMovie>> getAllMovies();

    @Delete
    void deleteAllMovies(List<FavoriteMovie> movies);

    @Delete
    void deleteMovie(FavoriteMovie favoriteMovie);

    @Query("SELECT movie_id FROM favorites_table")
    Cursor getIdMovies();

}
