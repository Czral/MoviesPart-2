package com.example.android.movies;

import android.app.LoaderManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.movies.Adapters.DetailFragmentAdapter;
import com.example.android.movies.Data.AppExecutors;
import com.example.android.movies.Data.FavoriteMovie;
import com.example.android.movies.Data.FavoriteRoomDatabase;
import com.example.android.movies.Data.MovieViewModel;
import com.example.android.movies.Files.Reviews;
import com.example.android.movies.Files.Video;
import com.example.android.movies.Loaders.ReviewsLoader;
import com.example.android.movies.Loaders.VideoLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XXX on 09-Aug-18.
 */

public class DetailsActivity extends AppCompatActivity {

    private final int REVIEW_LOADER_ID = 2;
    private final int VIDEO_LOADER_ID = 1;

    private String Title;
    private String OverView;
    private String Image;
    private String ReleaseDate;
    private double averageVote;
    private Bundle bundle;
    private int movieId;
    private ViewPager viewPager;

    private FavoriteRoomDatabase favoriteRoomDatabase;
    private MovieViewModel movieViewModel;
    private Intent favoriteIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_new);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setElevation(0);
        }

        Intent detailIntent = getIntent();
        movieId = detailIntent.getIntExtra(MainActivity.MOVIE_PATH, 0);
        Title = detailIntent.getStringExtra(MainActivity.TITLE);
        Image = detailIntent.getStringExtra(MainActivity.IMAGE);
        OverView = detailIntent.getStringExtra(MainActivity.OVERVIEW);
        ReleaseDate = detailIntent.getStringExtra(MainActivity.RELEASE);
        averageVote = detailIntent.getDoubleExtra(MainActivity.VOTE_AVERAGE, 0);

        bundle = new Bundle();
        bundle.putString(MainActivity.TITLE, Title);
        bundle.putString(MainActivity.IMAGE, Image);
        bundle.putString(MainActivity.OVERVIEW, OverView);
        bundle.putString(MainActivity.RELEASE, ReleaseDate);
        bundle.putDouble(MainActivity.VOTE_AVERAGE, averageVote);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(VIDEO_LOADER_ID, bundle, VideoLoader);
        loaderManager.initLoader(REVIEW_LOADER_ID, bundle, ReviewLoader);

        viewPager = findViewById(R.id.view_pager);

        DetailFragmentAdapter detailFragmentAdapter = new DetailFragmentAdapter(getSupportFragmentManager(), this, bundle);

        viewPager.setAdapter(detailFragmentAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        favoriteRoomDatabase = FavoriteRoomDatabase.getDatabase(getApplicationContext());
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
    }

    private LoaderManager.LoaderCallbacks<List<Video>> VideoLoader =
            new LoaderManager.LoaderCallbacks<List<Video>>() {
                @Override
                public Loader<List<Video>> onCreateLoader(int i, Bundle bundle) {

                    String string = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + MainActivity.API_KEY;

                    return new VideoLoader(DetailsActivity.this, string);
                }

                @Override
                public void onLoadFinished(Loader<List<Video>> loader, List<Video> data) {

                    ArrayList<Video> videos = new ArrayList<>();
                    if (data != null) {

                        for (int i = 0; i < data.size(); i++) {

                            Video video = data.get(i);
                            videos.add(video);
                        }

                    }
                    bundle.putParcelableArrayList(MainActivity.VIDEOS, videos);
                }

                @Override
                public void onLoaderReset(Loader<List<Video>> loader) {

                    bundle.remove(MainActivity.VIDEOS);

                }
            };

    private LoaderManager.LoaderCallbacks<List<Reviews>> ReviewLoader =
            new LoaderManager.LoaderCallbacks<List<Reviews>>() {
                @Override
                public Loader<List<Reviews>> onCreateLoader(int i, Bundle bundle) {

                    Uri uriReviews = Uri.parse("https://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=" + MainActivity.API_KEY);
                    return new ReviewsLoader(DetailsActivity.this, uriReviews.toString());
                }

                @Override
                public void onLoadFinished(Loader<List<Reviews>> loader, List<Reviews> data) {

                    ArrayList<Reviews> reviews = new ArrayList<>();

                    if (data != null) {

                        for (int i = 0; i < data.size(); i++) {

                            Reviews review = data.get(i);
                            reviews.add(review);
                        }
                    }

                    bundle.putParcelableArrayList(MainActivity.REVIEWS, reviews);
                }

                @Override
                public void onLoaderReset(Loader<List<Reviews>> loader) {

                }
            };


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.detail_activity_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        int itemSelected = item.getItemId();

        switch (itemSelected) {

            case R.id.share:

                Intent shareIntent = ShareCompat.IntentBuilder.from(this).
                        setChooserTitle("Check").
                        setType("text/plain").
                        setText(Title).
                        getIntent();
                if (shareIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(shareIntent);
                }
                break;

            case R.id.favorite:

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        FavoriteMovie favoriteMovie = new FavoriteMovie(Title, Image, OverView, ReleaseDate, averageVote, movieId);

                        Cursor cursor = favoriteRoomDatabase.movieDao().getIdMovies();

                        if (cursor == null || cursor.getCount() < 1) {

                            movieViewModel.insert(favoriteMovie);
                            favoriteIntent = new Intent(DetailsActivity.this, FavoriteActivity.class);
                            startActivity(favoriteIntent);

                        } else {

                            int idMovieColumn = cursor.getColumnIndex(FavoriteMovie.idColumn);

                            for (int i = 0; i < cursor.getCount(); i++) {

                                cursor.moveToPosition(i);
                                int movieDatabaseId = cursor.getInt(idMovieColumn);

                                if (movieDatabaseId == movieId) {

                                    Snackbar snackbar = Snackbar.make(viewPager, getString(R.string.already_favorite), BaseTransientBottomBar.LENGTH_SHORT);
                                    snackbar.show();
                                    cursor.close();
                                    return;
                                }
                            }
                            movieViewModel.insert(favoriteMovie);
                            favoriteIntent = new Intent(DetailsActivity.this, FavoriteActivity.class);
                            cursor.close();
                            startActivity(favoriteIntent);

                        }

                    }
                });

                break;

            case android.R.id.home:

                Intent backIntent = new Intent(DetailsActivity.this, MainActivity.class);
                startActivity(backIntent);
        }

        return true;
    }
}
