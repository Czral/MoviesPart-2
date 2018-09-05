package com.example.android.movies.Files;

/**
 * Created by XXX on 25-Jun-18.
 */

public class MovieFile {

    private int id;
    private String title;
    private String image;
    private double voteAverage;
    private String overview;
    private String date;

    public MovieFile(int path, String title, String image, double voteAverage, String overview, String date) {

        this.id = path;
        this.title = title;
        this.image = image;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.date = date;
    }

    public int getId() {

        return id;
    }

    public String getTitle() {

        return title;
    }

    public String getImage() {

        return image;
    }

    public double getVoteAverage() {

        return voteAverage;
    }

    public String getOverview() {

        return overview;
    }

    public String getDate() {

        return date;
    }

}
