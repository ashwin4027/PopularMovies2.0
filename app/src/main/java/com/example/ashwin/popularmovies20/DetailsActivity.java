package com.example.ashwin.popularmovies20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG= DetailsActivity.class.getSimpleName();
    private TextView titleTextView;
    private ImageView posterImageView;
    private TextView userRatingTextView;
    private TextView synopsisTextView;
    private TextView releaseDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String title= getIntent().getStringExtra("title");
        String poster= getIntent().getStringExtra("poster");
        String releaseDate= getIntent().getStringExtra("releaseDate");
        String synopsis= getIntent().getStringExtra("synopsis");
        String userRating= getIntent().getStringExtra("averageRating");


        titleTextView= (TextView) findViewById(R.id.details_title);
        posterImageView= (ImageView) findViewById(R.id.details_poster);
        userRatingTextView= (TextView) findViewById(R.id.details_user_rating);
        synopsisTextView= (TextView) findViewById(R.id.details_synopsis);
        releaseDateTextView= (TextView) findViewById(R.id.details_release_date);


        titleTextView.setText(title);
        userRatingTextView.setText("User Rating: "+userRating);
        releaseDateTextView.setText("Release Date: "+releaseDate);
        synopsisTextView.setText("Synopsis:\n"+synopsis);
        Picasso.with(this).load(poster).into(posterImageView);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();

        if(id == R.id.action_settings)
        {
            Intent settingsIntent= new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
