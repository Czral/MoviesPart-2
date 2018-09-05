package com.example.android.movies.Files;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by XXX on 11-Aug-18.
 */

public class Reviews implements Parcelable {

    private String author;
    private String content;
    private String url;

    public Reviews(String mAuthor, String mContent, String mUrl) {

        author = mAuthor;
        content = mContent;
        url = mUrl;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    protected Reviews(Parcel in) {
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Reviews> CREATOR = new Parcelable.Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel in) {
            return new Reviews(in);
        }

        @Override
        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };
}
