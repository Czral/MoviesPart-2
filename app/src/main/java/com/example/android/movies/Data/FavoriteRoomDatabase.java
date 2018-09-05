package com.example.android.movies.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by XXX on 05-Aug-18.
 */

@Database(entities = {FavoriteMovie.class}, version = 1, exportSchema = false)
public abstract class FavoriteRoomDatabase extends RoomDatabase {

    private static FavoriteRoomDatabase INSTANCE;

    public abstract MovieDao movieDao();

    public static FavoriteRoomDatabase getDatabase(Context context) {

        if (INSTANCE == null) {

            synchronized (FavoriteRoomDatabase.class) {

                INSTANCE = Room.databaseBuilder(context,
                        FavoriteRoomDatabase.class,
                        "movies_database").
                        build();
            }
        }

        return INSTANCE;
    }

}
