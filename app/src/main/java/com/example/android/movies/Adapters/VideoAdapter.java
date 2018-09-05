package com.example.android.movies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.movies.Files.Video;
import com.example.android.movies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by XXX on 12-Aug-18.
 */

public class VideoAdapter extends ArrayAdapter<Video> {

    private Context mContext;

    public VideoAdapter(Context context, List<Video> videos) {

        super(context, 0, videos);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if (view == null) {

            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        final Video currentVideo = getItem(position);

        final String url = currentVideo.getVideoUrl();
        final String videoUrl = currentVideo.getVideoThumbnail();

        Uri uri = Uri.parse(url);

        ImageView imageView = view.findViewById(R.id.image_view_list);
        Picasso.get().load(uri).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(videoUrl));
                mContext.startActivity(intent);
            }
        });

        return view;
    }


}
