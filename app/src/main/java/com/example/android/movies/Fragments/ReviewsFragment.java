package com.example.android.movies.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.movies.Adapters.ReviewAdapter;
import com.example.android.movies.Files.Reviews;
import com.example.android.movies.MainActivity;
import com.example.android.movies.R;

import java.util.ArrayList;

/**
 * Created by XXX on 09-Aug-18.
 */

public class ReviewsFragment extends android.support.v4.app.Fragment {

    public ReviewsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        ArrayList<Reviews> reviews = bundle.getParcelableArrayList(MainActivity.REVIEWS);

        View view = inflater.inflate(R.layout.list_reviews_videos, container, false);
        final ReviewAdapter reviewAdapter = new ReviewAdapter(view.getContext(), reviews);

        GridView listView = view.findViewById(R.id.reviews_list);
        listView.setNumColumns(1);

        listView.setAdapter(reviewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Reviews review = reviewAdapter.getItem(position);
                intent.setData(Uri.parse(review.getUrl()));

                startActivity(intent);

            }
        });

        registerForContextMenu(view);

        return view;
    }

}
