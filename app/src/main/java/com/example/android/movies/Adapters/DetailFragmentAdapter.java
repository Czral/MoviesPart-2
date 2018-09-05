package com.example.android.movies.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.movies.Fragments.DetailsFragment;
import com.example.android.movies.Fragments.ReviewsFragment;
import com.example.android.movies.Fragments.VideosFragment;
import com.example.android.movies.R;

/**
 * Created by XXX on 09-Aug-18.
 */

public class DetailFragmentAdapter extends FragmentPagerAdapter {

    private Context context;
    private Bundle bundle;

    public DetailFragmentAdapter(android.support.v4.app.FragmentManager fragmentManager, Context c, Bundle b) {

        super(fragmentManager);
        context = c;
        bundle = b;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {

            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setArguments(this.bundle);
            return detailsFragment;
        }
        if (position == 1) {

            VideosFragment videosFragment = new VideosFragment();
            videosFragment.setArguments(this.bundle);
            return videosFragment;
        }
        if (position == 2) {

            ReviewsFragment reviewsFragment = new ReviewsFragment();
            reviewsFragment.setArguments(this.bundle);
            return reviewsFragment;
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = "";

        if (position == 0) {
            title = context.getResources().getString(R.string.details);
        } else if (position == 1) {
            title = context.getResources().getString(R.string.videos);
        } else if (position == 2) {
            title = context.getResources().getString(R.string.reviews);
        }

        return title;
    }
}
