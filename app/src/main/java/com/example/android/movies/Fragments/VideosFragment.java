package com.example.android.movies.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.android.movies.Adapters.VideoAdapter;
import com.example.android.movies.Files.Video;
import com.example.android.movies.MainActivity;
import com.example.android.movies.R;

import java.util.ArrayList;

/**
 * Created by XXX on 09-Aug-18.
 */

public class VideosFragment extends Fragment {

    private GridView listView;
    private View view;

    public VideosFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list_reviews_videos, container, false);

        listView = view.findViewById(R.id.reviews_list);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            listView.setNumColumns(3);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            listView.setNumColumns(2);
        }

        registerForContextMenu(view);

        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if ((isVisibleToUser) && (getContext() != null)) {

            Bundle bundle = getArguments();
            try {
                ArrayList<Video> videos = bundle.getParcelableArrayList(MainActivity.VIDEOS);

                VideoAdapter videoAdapter = new VideoAdapter(view.getContext(), videos);
                listView.setAdapter(videoAdapter);


            } catch (NullPointerException e) {

                e.printStackTrace();
            }


        }

    }

}
