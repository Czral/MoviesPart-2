package com.example.android.movies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.movies.Files.Reviews;
import com.example.android.movies.R;

import java.util.List;

/**
 * Created by XXX on 11-Aug-18.
 */

public class ReviewAdapter extends ArrayAdapter<Reviews> {

    public ReviewAdapter(Context context, List<Reviews> reviews) {

        super(context, 0, reviews);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listView = convertView;

        if (listView == null) {

            listView = LayoutInflater.from(getContext()).inflate(R.layout.activity_reviews, parent, false);
        }

        Reviews currentReviews = getItem(position);

        TextView authorTextView = listView.findViewById(R.id.author);
        authorTextView.setText(currentReviews.getAuthor());

        TextView contentTextView = listView.findViewById(R.id.content);
        contentTextView.setText(currentReviews.getContent());

        return listView;
    }
}
