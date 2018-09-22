package com.example.android.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movies.Adapters.GridAdapter;
import com.example.android.movies.Files.MovieFile;
import com.example.android.movies.Loaders.MoviesLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        android.app.LoaderManager.LoaderCallbacks<List<MovieFile>> {

    private static final String BASE = "https://api.themoviedb.org/3";
    private static final String DISCOVER = "discover";
    private static final String MOVIE = "movie";
    private static final String SEARCH = "search";
    private static final String KEYWORD = "keyword";
    private static final String GENRE = "genre";
    private static final String SORT_BY = "sort_by";
    private static final String WITH_GENRES = "with_genres";
    private static final String QUERY = "query";
    private static final String API = "api_key";

    //TODO: Insert API-KEY here:
    public static final String API_KEY = "090b775fc2af47d4507247171f907d56";
    private static final String PAGE = "page";

    public static final String RELEASE = "RELEASE";
    public static final String TITLE = "TITLE";
    public static final String MOVIE_PATH = "MOVIE_PATH";
    public static final String IMAGE = "IMAGE";
    public static final String VOTE_AVERAGE = "VOTE_AVERAGE";
    public static final String OVERVIEW = "OVERVIEW";
    public static final String REVIEWS = "REVIEWS";
    public static final String VIDEOS = "VIDEOS";

    private static final int LOADER_ID = 87;

    private static final String TAG = MainActivity.class.getName();

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.grid_view)
    GridView listView;

    @BindView(R.id.empty)
    LinearLayout emptyLayout;

    @BindView(R.id.page_text_view)
    TextView pageTextView;

    @BindView(R.id.text_view)
    TextView emptyTextView;

    private GridAdapter adapter;

    private android.app.LoaderManager loaderManager;

    private int i;

    private String keyword;
    private String genre = "";
    private String sortBy = "";

    private String currentPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            listView.setNumColumns(3);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            listView.setNumColumns(2);
        }

        listView.setEmptyView(emptyLayout);

        i++;

        setup();

        adapter = new GridAdapter(this, new ArrayList<MovieFile>());

        loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MovieFile movieFile = (MovieFile) adapter.getItem(position);

                String title = movieFile.getTitle();
                int movieId = movieFile.getId();
                String overview = movieFile.getOverview();
                String image = movieFile.getImage();
                String releaseDate = movieFile.getDate();
                double voteAverage = movieFile.getVoteAverage();

                Intent detailIntent = new Intent(MainActivity.this, DetailsActivity.class);

                detailIntent.putExtra(TITLE, title);

                detailIntent.putExtra(OVERVIEW, overview);
                detailIntent.putExtra(IMAGE, image);
                detailIntent.putExtra(MOVIE_PATH, movieId);
                detailIntent.putExtra(RELEASE, releaseDate);
                detailIntent.putExtra(VOTE_AVERAGE, voteAverage);

                startActivity(detailIntent);

            }
        });

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        switch (key) {

            case KEYWORD:

                keyword = sharedPreferences.getString(key, "");
                break;

            case SORT_BY:

                sortBy = sharedPreferences.getString(key, getResources().getString(R.string.popularity_value));
                break;

            case GENRE:

                genre = sharedPreferences.getString(key, getResources().getString(R.string.all));
                break;
        }

        onLoaderRestart();
    }

    @Override
    public android.content.Loader<List<MovieFile>> onCreateLoader(int id, Bundle args) {

        if (isOnline()) {

            emptyLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            pageTextView.setText(getString(R.string.page) + String.valueOf(i));

            return new MoviesLoader(this, buildUrl(keyword, genre, sortBy).toString());

        } else {

            emptyTextView.setText(getString(R.string.no_internet_connection_error));
            emptyLayout.setVisibility(View.VISIBLE);
            return null;
        }

    }

    @Override
    public void onLoadFinished(android.content.Loader<List<MovieFile>> loader, List<MovieFile> list) {

        adapter.notifyDataSetChanged();

        if (list != null && !list.isEmpty()) {

            progressBar.setVisibility(View.GONE);
            adapter = new GridAdapter(this, (ArrayList<MovieFile>) list);
            listView.setAdapter(adapter);
        } else {

            emptyTextView.setText(getString(R.string.no_results_error));
            emptyLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(android.content.Loader<List<MovieFile>> loader) {

        i = 1;
        adapter.notifyDataSetChanged();
    }

    public void onLoaderRestart() {

        if (isOnline()) {

            progressBar.setVisibility(View.VISIBLE);
            loaderManager.restartLoader(LOADER_ID, null, this);
        } else {

            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).
                unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemSelected = item.getItemId();

        switch (itemSelected) {

            case R.id.next_page:

                buildUrl(keyword, genre, sortBy);

                startNextPage();
                onLoaderRestart();
                break;

            case R.id.previous_page:

                startPreviousPage();
                onLoaderRestart();
                break;

            case R.id.search:

                Intent searchIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(searchIntent);
                break;

            case R.id.refresh:

                onLoaderRestart();
                break;

            case R.id.favorites:

                Intent favoriteIntent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(favoriteIntent);
        }

        return true;

    }

    private boolean isOnline() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connection = networkInfo != null && networkInfo.isConnected();
        return connection;

    }

    private String buildUrl(String k, String g, String s) {

        Uri movieUrl = Uri.parse(BASE);
        Uri.Builder uriBuilder = movieUrl.buildUpon();

        if (g.equals(getString(R.string.all))) {

            g = "";
        }
        if (!k.equals("") && !(k == null)) {

            String[] parts;

            parts = k.split("\\s+");
            currentPart = parts[0];
            if (k.contains(" ")) {

                for (int m = 1; m < parts.length; m++) {

                    currentPart += "+" + parts[m];
                }
            }
        }
        if (k.equals("")) {

            movieUrl = uriBuilder.
                    appendPath(DISCOVER).
                    appendPath(MOVIE).
                    appendQueryParameter(WITH_GENRES, g).
                    appendQueryParameter(SORT_BY, s).
                    appendQueryParameter(API, API_KEY).
                    appendQueryParameter(PAGE, String.valueOf(i)).
                    build();
        } else {

            movieUrl = uriBuilder.
                    appendPath(SEARCH).
                    appendPath(MOVIE).
                    appendQueryParameter(QUERY, currentPart).
                    appendQueryParameter(WITH_GENRES, g).
                    appendQueryParameter(SORT_BY, s).
                    appendQueryParameter(API, API_KEY).
                    appendQueryParameter(PAGE, String.valueOf(i)).
                    build();

        }

        return movieUrl.toString();
    }

    private void setup() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        keyword = sharedPreferences.getString(KEYWORD, "");
        sortBy = sharedPreferences.getString(getResources().getString(R.string.sort_by), getString(R.string.popularity_value));
        genre = sharedPreferences.getString(getResources().getString(R.string.genre), getString(R.string.all));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private boolean startNextPage() {

        i++;

        pageTextView.setText(getString(R.string.page) + String.valueOf(i));
        return true;
    }

    private boolean startPreviousPage() {

        if (i > 1) {

            i--;
            pageTextView.setText(getString(R.string.page) + String.valueOf(i));
        } else {

            Toast.makeText(this, getString(R.string.this_is_the_first_page_info), Toast.LENGTH_SHORT).show();
        }

        return true;
    }

}
