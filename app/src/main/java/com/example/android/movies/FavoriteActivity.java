package com.example.android.movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.movies.Adapters.FavoriteAdapter;
import com.example.android.movies.Data.AppExecutors;
import com.example.android.movies.Data.FavoriteMovie;
import com.example.android.movies.Data.FavoriteRoomDatabase;
import com.example.android.movies.Data.MovieViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends AppCompatActivity {

    @BindView(R.id.list)
    ListView listView;

    private FavoriteAdapter adapter;

    private FavoriteRoomDatabase favoriteRoomDatabase;
    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        ButterKnife.bind(this);

        adapter = new FavoriteAdapter(this);
        listView.setAdapter(adapter);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getAllMovies().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovie> favoriteMovies) {

                adapter.setFavoriteMovies(favoriteMovies);
            }
        });

        favoriteRoomDatabase = FavoriteRoomDatabase.getDatabase(getApplicationContext());

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        List<FavoriteMovie> favoriteMovies = adapter.getFavoriteMovies();
                        favoriteRoomDatabase.movieDao().deleteMovie(favoriteMovies.get(i));
                    }
                });

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                List<FavoriteMovie> favoriteMovies = adapter.getFavoriteMovies();
                FavoriteMovie favoriteMovie = favoriteMovies.get(position);
                int movieId = favoriteMovie.getMovieId();

                String stringUri = "https://www.themoviedb.org/movie/" + movieId;

                Intent openIntent = new Intent(Intent.ACTION_VIEW);
                openIntent.setData(Uri.parse(stringUri));
                startActivity(openIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.favorites_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.delete:

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        List<FavoriteMovie> movies = adapter.getFavoriteMovies();
                        favoriteRoomDatabase.movieDao().deleteAllMovies(movies);
                    }
                });
                break;

            case android.R.id.home:

                Intent main = new Intent(FavoriteActivity.this, MainActivity.class);
                startActivity(main);
        }

        return true;
    }
}
