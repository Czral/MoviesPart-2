package com.example.android.movies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.movies.Files.MovieFile;
import com.example.android.movies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by XXX on 25-Jun-18.
 */

public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MovieFile> movieFileArrayList;

    public GridAdapter(Context context, ArrayList<MovieFile> movieFiles) {

        mContext = context;
        movieFileArrayList = movieFiles;
    }

    @Override
    public int getCount() {
        return movieFileArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieFileArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView = convertView;

        if (gridView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = layoutInflater.inflate(R.layout.list_item, null);
        }

        ImageView imageView = gridView.findViewById(R.id.image_view_list);

        if (movieFileArrayList.get(position) == null || movieFileArrayList.get(position).getImage() == null) {
            imageView.setImageResource(R.drawable.no_poster);
        } else {
            Picasso.get().
                    load("http://image.tmdb.org/t/p/w500/" + movieFileArrayList.get(position).getImage())
                    .error(R.drawable.no_poster).into(imageView);

        }
        return gridView;
    }
}
