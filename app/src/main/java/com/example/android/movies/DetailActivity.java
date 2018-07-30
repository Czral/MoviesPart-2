package com.example.android.movies;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.plot_synopsis)
    TextView overview;

    @BindView(R.id.title_movie)
    TextView titleView;

    @BindView(R.id.vote_rating_bar)
    RatingBar ratingBar;

    @BindView(R.id.release_date)
    TextView releaseView;

    @BindView(R.id.poster)
    ImageView imageView;

    String Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Intent detailIntent = getIntent();

        Title = detailIntent.getStringExtra(MainActivity.TITLE);
        String OverView = detailIntent.getStringExtra(MainActivity.OVERVIEW);
        String Image = detailIntent.getStringExtra(MainActivity.IMAGE);

        String Release = detailIntent.getStringExtra(MainActivity.RELEASE);
        double averageVote = detailIntent.getDoubleExtra(MainActivity.OVERVIEW, 0);
        float Average = Float.valueOf(String.valueOf(averageVote));
        String ReleaseDate = parseDate(Release);

        overview.setMovementMethod(new ScrollingMovementMethod());
        Picasso.get().load("http://image.tmdb.org/t/p/w500/" + Image).into(imageView);
        overview.setText(OverView);
        releaseView.setText(ReleaseDate);
        ratingBar.setRating(Average);
        titleView.setText(Title);

    }

    public String parseDate(String dateString) {
        String input = "yyyy-MM-dd";
        String output = "dd-MMM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(input);
        SimpleDateFormat outputFormat = new SimpleDateFormat(output);
        Date date;
        String finalDate = null;
        try {
            date = dateFormat.parse(dateString);
            finalDate = outputFormat.format(date);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return finalDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.detail_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemSelected = item.getItemId();

        switch (itemSelected) {

            case R.id.share:

                Intent intent = ShareCompat.IntentBuilder.from(this).
                        setType("text/plain").
                        setText(Title).
                        getIntent();
                item.setIntent(intent);

            case android.R.id.home:

                Intent backIntent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(backIntent);

        }

        return true;
    }

}
