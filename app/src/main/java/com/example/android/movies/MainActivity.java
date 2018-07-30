package com.example.android.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

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
    private static final String API_KEY = "090b775fc2af47d4507247171f907d56";
    private static final String PAGE = "page";

    public static final String RELEASE = "RELEASE";
    public static final String TITLE = "TITLE";
    public static final String IMAGE = "IMAGE";
    public static final String VOTE_AVERAGE = "VOTEAVERAGE";
    public static final String OVERVIEW = "OVERVIEW";

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.grid_view)
    GridView listView;

    @BindView(R.id.empty)
    LinearLayout linearLayout;

    private ArrayList<ImageMovie> arrayList;

    private GridAdapter adapter;

    Uri movieUrl = Uri.parse(BASE);
    Uri.Builder uriBuilder = movieUrl.buildUpon();

    int i;

    private String keyword;
    private String genre;
    private String sortBy;
    private String[] parts;
    private String currentPart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        listView.setEmptyView(linearLayout);

        i++;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        buildUrl(sharedPreferences);

    }

    public class FetchMovieData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            if (isOnline()) {
                linearLayout.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                super.onPreExecute();
            } else {

                linearLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                getResponseFromHttpUrl(Uri.parse(strings[0]));
                return getResponseFromHttpUrl(Uri.parse(strings[0]));

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String strings) {

            progressBar.setVisibility(View.INVISIBLE);
            getDataFromJson(strings);
            super.onPostExecute(strings);
        }
    }

    private String getResponseFromHttpUrl(Uri uri) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        InputStream inputStream = null;
        URL url = new URL(uri.toString());
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setReadTimeout(15000);
        httpURLConnection.setConnectTimeout(15000);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        try {
            inputStream = httpURLConnection.getInputStream();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();

                }
            }

        } finally {

            httpURLConnection.disconnect();
            inputStream.close();
        }
        return stringBuilder.toString();
    }

    public void getDataFromJson(String string) {

        arrayList = new ArrayList();
        try {

            JSONObject currentMovie = new JSONObject(string);
            JSONArray resultArray = currentMovie.getJSONArray("results");

            if (resultArray.length() == 0) {

                TextView textView = findViewById(R.id.text_view);
                textView.setText("No more results");
                ImageView imageView = findViewById(R.id.image_view);
                imageView.setVisibility(View.INVISIBLE);
            }

            for (int i = 0; i <= resultArray.length(); i++) {

                JSONObject movie = resultArray.getJSONObject(i);
                String idMovie = movie.getString("backdrop_path");
                String titleMovie = movie.getString("original_title");
                String imageMovie = movie.getString("poster_path");
                double voteAverageMovie = movie.getDouble("vote_average");
                String overviewMovie = movie.getString("overview");
                String releaseDateMovie = movie.getString("release_date");

                arrayList.add(new ImageMovie(idMovie, titleMovie, imageMovie, voteAverageMovie, overviewMovie, releaseDateMovie));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        ImageMovie movie = (ImageMovie) adapter.getItem(position);
                        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);

                        String release = movie.getReleaseDate();
                        String title = movie.getName();
                        String overview = movie.getOverview();
                        double average = movie.getVoteAverage();
                        String image = movie.getImageUrl();

                        detailIntent.putExtra(RELEASE, release);
                        detailIntent.putExtra(TITLE, title);
                        detailIntent.putExtra(IMAGE, image);
                        detailIntent.putExtra(OVERVIEW, overview);
                        detailIntent.putExtra(VOTE_AVERAGE, average);

                        startActivity(detailIntent);

                    }
                });
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }

        adapter = new GridAdapter(this, arrayList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Uri movieUrlNext = Uri.parse(BASE);
        Uri.Builder uriBuilderNext = movieUrlNext.buildUpon();

        int itemSelected = item.getItemId();

        switch (itemSelected) {

            case R.id.next_page:

                if (isOnline()) {

                    i++;
                    if (keyword.equals("") || keyword == null) {

                        movieUrlNext = uriBuilderNext.
                                appendPath(DISCOVER).
                                appendPath(MOVIE).
                                appendQueryParameter(WITH_GENRES, genre).
                                appendQueryParameter(SORT_BY, sortBy).
                                appendQueryParameter(API, API_KEY).
                                appendQueryParameter(PAGE, String.valueOf(i)).
                                build();

                    } else {

                        movieUrlNext = uriBuilderNext.
                                appendPath(SEARCH).
                                appendPath(MOVIE).
                                appendQueryParameter(QUERY, currentPart).
                                appendQueryParameter(WITH_GENRES, genre).
                                appendQueryParameter(SORT_BY, sortBy).
                                appendQueryParameter(API, API_KEY).
                                appendQueryParameter(PAGE, String.valueOf(i)).
                                build();

                    }

                    new FetchMovieData().execute(movieUrlNext.toString());
                    return true;
                } else {

                    listView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    return true;
                }
            case R.id.previous_page:

                if (isOnline() && i > 1) {

                    i--;
                    if (keyword.equals("") || keyword.isEmpty()) {

                        movieUrlNext = uriBuilderNext.
                                appendPath(DISCOVER).
                                appendPath(MOVIE).
                                appendQueryParameter(WITH_GENRES, genre).
                                appendQueryParameter(SORT_BY, sortBy).
                                appendQueryParameter(API, API_KEY).
                                appendQueryParameter(PAGE, String.valueOf(i)).
                                build();
                    } else {

                        movieUrlNext = uriBuilderNext.
                                appendPath(SEARCH).
                                appendPath(MOVIE).
                                appendQueryParameter(QUERY, currentPart).
                                appendQueryParameter(WITH_GENRES, genre).
                                appendQueryParameter(SORT_BY, sortBy).
                                appendQueryParameter(API, API_KEY).
                                appendQueryParameter(PAGE, String.valueOf(i)).
                                build();
                    }

                    new FetchMovieData().execute(movieUrlNext.toString());
                    return true;
                } else {

                    Toast.makeText(this, "This is the first page.", Toast.LENGTH_SHORT).show();
                    listView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    return true;

                }

            case R.id.search:

                if (isOnline()) {

                    Intent searchIntent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(searchIntent);
                    return true;

                } else {

                    //                      emptyStateTextView.setVisibility(View.VISIBLE);
                    return true;
                }

            case R.id.refresh:

                if (isOnline()) {

                    if (keyword.equals("") || keyword == null) {

                        movieUrlNext = uriBuilderNext.
                                appendPath(DISCOVER).
                                appendPath(MOVIE).
                                appendQueryParameter(WITH_GENRES, genre).
                                appendQueryParameter(SORT_BY, sortBy).
                                appendQueryParameter(API, API_KEY).
                                appendQueryParameter(PAGE, String.valueOf(i)).
                                build();
                    } else {

                        movieUrlNext = uriBuilderNext.
                                appendPath(SEARCH).
                                appendPath(MOVIE).
                                appendQueryParameter(QUERY, currentPart).
                                appendQueryParameter(WITH_GENRES, genre).
                                appendQueryParameter(SORT_BY, sortBy).
                                appendQueryParameter(API, API_KEY).
                                appendQueryParameter(PAGE, String.valueOf(i)).
                                build();
                    }

                    new FetchMovieData().execute(movieUrlNext.toString());
                    return true;
                } else {

                    listView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    return true;
                }

        }

        return true;

    }

    private boolean isOnline() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connection = networkInfo != null && networkInfo.isConnected();
        return connection;

    }

    private void buildUrl(SharedPreferences sharedPreferences) {

        keyword = sharedPreferences.getString(KEYWORD, "").trim();
        genre = sharedPreferences.getString(GENRE, "");
        sortBy = sharedPreferences.getString(SORT_BY, "");

        if (genre.equals(getString(R.string.all))) {

            genre = "";
        }

        if (!keyword.equals("") && !(keyword == null)) {

            parts = keyword.split("\\s+");
            currentPart = parts[0];
            if (keyword.contains(" ")) {

                for (int k = 1; k < parts.length; k++) {

                    currentPart += "+" + parts[k];
                }

            }
        }

        if (isOnline()) {

            if (keyword.equals("") || keyword == null) {

                movieUrl = uriBuilder.
                        appendPath(DISCOVER).
                        appendPath(MOVIE).
                        appendQueryParameter(WITH_GENRES, genre).
                        appendQueryParameter(SORT_BY, sortBy).
                        appendQueryParameter(API, API_KEY).
                        appendQueryParameter(PAGE, String.valueOf(i)).
                        build();
            } else {

                movieUrl = uriBuilder.
                        appendPath(SEARCH).
                        appendPath(MOVIE).
                        appendQueryParameter(QUERY, currentPart).
                        appendQueryParameter(WITH_GENRES, genre).
                        appendQueryParameter(SORT_BY, sortBy).
                        appendQueryParameter(API, API_KEY).
                        appendQueryParameter(PAGE, String.valueOf(i)).
                        build();

            }

            new FetchMovieData().execute(movieUrl.toString());
        } else {

            linearLayout.setVisibility(View.VISIBLE);
        }


    }

}
