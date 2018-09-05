package com.example.android.movies.Files;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by XXX on 12-Aug-18.
 */

public class Video implements Parcelable {

    private String videoUrl;
    private String videoThumbnail;

    public Video(String url, String thumbnail) {

        videoUrl = url;
        videoThumbnail = thumbnail;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }


    protected Video(Parcel in) {
        videoUrl = in.readString();
        videoThumbnail = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoUrl);
        dest.writeString(videoThumbnail);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}