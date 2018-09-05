package com.example.android.movies.Data;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.movies.Files.MovieFile;
import com.example.android.movies.Files.Reviews;
import com.example.android.movies.Files.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XXX on 04-Aug-18.
 */

public final class MovieUtils {

    private static final String RESULTS = "results";
    private static final String GET_METHOD = "GET";
    private static final String KEY = "key";
    private static final String CONTENT = "content";
    private static final String AUTHOR = "author";
    private static final String URL = "url";

    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String POSTER_PATH = "poster_path";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String MOVIE_ID = "id";

    private static final int TIMEOUT = 15000;
    private static final String CLASS_TAG = MovieUtils.class.getName();

    private MovieUtils() {

    }

    private static URL createUrl(String urlString) {

        URL url = null;

        try {

            url = new URL(urlString);
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
        return url;
    }


    private static String getResponseFromHttpUrl(URL url) throws IOException {

        String jsonResponse = "";

        if (url == null) {

            return jsonResponse;
        } else {
            InputStream inputStream = null;
            HttpURLConnection httpURLConnection = null;

            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(TIMEOUT);
                httpURLConnection.setConnectTimeout(TIMEOUT);
                httpURLConnection.setRequestMethod(GET_METHOD);
                httpURLConnection.connect();

                if (httpURLConnection.getResponseCode() == 200) {

                    inputStream = httpURLConnection.getInputStream();
                    jsonResponse = getDataFromJson(inputStream);

                } else {

                    Log.d(CLASS_TAG, "Error with response code" + httpURLConnection.getResponseCode());
                }
            } catch (IOException e) {

                e.printStackTrace();
            } finally {

                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            }

            return jsonResponse;
        }

    }

    private static String getDataFromJson(InputStream inputStream) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();

            }
        }

        return stringBuilder.toString();
    }

    public static List<Video> extractVideosFromJson(String string) {

        List<Video> videos = new ArrayList<>();

        try {
            JSONObject movie = new JSONObject(string);
            JSONArray results = movie.getJSONArray(RESULTS);
            if (results.length() == 0) {

                Log.d(CLASS_TAG, "Empty Arryalist");
            } else {

                for (int i = 0; i < results.length(); i++) {

                    JSONObject videoObject = results.getJSONObject(i);
                    String key = videoObject.getString(KEY);
                    String imageUri = String.format("http://img.youtube.com/vi/%1$s/0.jpg", key);
                    String videoUri = String.format("http://youtube.com/watch?v=%1$s", key);
                    videos.add(new Video(imageUri, videoUri));

                }
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }

        return videos;
    }


    public static List<Reviews> extractReviewsFromJson(String string) {

        List<Reviews> reviews = new ArrayList<>();

        try {
            JSONObject movie = new JSONObject(string);
            JSONArray results = movie.getJSONArray(RESULTS);
            if (results.length() == 0) {

                Log.d(CLASS_TAG, "Empty reviews Arraylist");
            } else {

                for (int i = 0; i < results.length(); i++) {

                    JSONObject reviewObject = results.getJSONObject(i);
                    String content = reviewObject.getString(CONTENT);
                    String author = reviewObject.getString(AUTHOR);
                    String url = reviewObject.getString(URL);

                    reviews.add(new Reviews(author, content, url));

                }
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }

        return reviews;
    }

    public static List<MovieFile> extractFeaturesFromJson(String moviesJson) {

        List<MovieFile> list = new ArrayList<>();

        int idMovie;
        String titleMovie;
        String imageMovie;
        double voteAverageMovie = 0;
        String overviewMovie = "";
        String releaseDateMovie = "";

        if (TextUtils.isEmpty(moviesJson)) {

            Log.d(CLASS_TAG, "EMPTY JSON!!!");
            return null;
        }

        try {

            JSONObject currentMovie = new JSONObject(moviesJson);
            JSONArray resultArray = currentMovie.getJSONArray(RESULTS);

            if (resultArray.length() == 0) {

                Log.d(CLASS_TAG, "Empty ARRAYLIST!!!");
            } else {

                for (int i = 0; i < resultArray.length(); i++) {

                    JSONObject movie = resultArray.getJSONObject(i);
                    idMovie = movie.getInt(MOVIE_ID);
                    titleMovie = movie.getString(ORIGINAL_TITLE);
                    imageMovie = movie.getString(POSTER_PATH);
                    voteAverageMovie = movie.getDouble(VOTE_AVERAGE);
                    overviewMovie = movie.getString(OVERVIEW);
                    releaseDateMovie = movie.getString(RELEASE_DATE);

                    MovieFile movieFile = new MovieFile(idMovie,
                            titleMovie,
                            imageMovie,
                            voteAverageMovie,
                            overviewMovie,
                            releaseDateMovie);

                    list.add(movieFile);

                }

            }

        } catch (JSONException e) {

            e.printStackTrace();
        }

        return list;
    }

    public static List<MovieFile> fetchMovieData(String urlString) {

        URL url = createUrl(urlString);
        String jsonResponse = null;

        try {

            jsonResponse = getResponseFromHttpUrl(url);
        } catch (IOException e) {

            e.printStackTrace();
        }

        List<MovieFile> movieFileList = extractFeaturesFromJson(jsonResponse);
        return movieFileList;
    }

    public static List<Reviews> fetchReviews(String string) {

        URL url = createUrl(string);

        String jsonReviews = null;

        try {
            jsonReviews = getResponseFromHttpUrl(url);
        } catch (IOException e) {

            e.printStackTrace();
        }

        List<Reviews> results = extractReviewsFromJson(jsonReviews);

        return results;

    }

    public static List<Video> fetchVideos(String string) {

        URL url = createUrl(string);
        String jsonVideos = null;

        try {
            jsonVideos = getResponseFromHttpUrl(url);
        } catch (IOException e) {

            e.printStackTrace();
        }

        List<Video> results = extractVideosFromJson(jsonVideos);
        return results;
    }

}