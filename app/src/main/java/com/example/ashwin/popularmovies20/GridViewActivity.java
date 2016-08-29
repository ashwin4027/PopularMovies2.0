package com.example.ashwin.popularmovies20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ashwin on 8/21/16.
 */
public class GridViewActivity extends AppCompatActivity {
    private static final String LOG_TAG= GridViewActivity.class.getSimpleName();
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridViewAdapter;
    private ArrayList<GridItem> movieData;
    private String FEED_URL="https://api.themoviedb.org/3/movie/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView= (GridView) findViewById(R.id.posterGridView);
        mProgressBar= (ProgressBar) findViewById(R.id.progressBar);

        //Initializing with empty data set
        movieData= new ArrayList<>();
        mGridViewAdapter = new GridViewAdapter(this, R.layout.grid_view_item, movieData);
        mGridView.setAdapter(mGridViewAdapter);

        //Start Download
        updateMovieList();
        mProgressBar.setVisibility(View.VISIBLE);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridItem item= (GridItem) parent.getItemAtPosition(position);

                Intent detailsIntent= new Intent(GridViewActivity.this, DetailsActivity.class);
                detailsIntent.putExtra("title", item.getTitle());
                detailsIntent.putExtra("poster", item.getImage());
                detailsIntent.putExtra("synopsis", item.getSynopsis());
                detailsIntent.putExtra("averageRating", item.getAverageRating());
                detailsIntent.putExtra("releaseDate", item.getReleaseDate());

                startActivity(detailsIntent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();

        if(id == R.id.action_settings)
        {
            Intent settingsIntent= new Intent(getApplicationContext(), SettingsActivity.class);
            startActivityForResult(settingsIntent,1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateMovieList()
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplication());
        String sortBy= pref.getString(getString(R.string.pref_key_sortType), getString(R.string.pref_default_sortType));
        Log.d(LOG_TAG, "Sort By: "+sortBy);

        AsyncHttpTask fetchMovies= new AsyncHttpTask();
        fetchMovies.execute(FEED_URL, sortBy);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        updateMovieList();
    }




    //Downloading data asynchronously

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            Integer result=0;
            HttpURLConnection urlConnection= null;
            BufferedReader reader= null;
            String movieJsonStr= null;



            try {
                //Creating URL
                final String MOVIEDB_BASE_URL= params[0];
                final String SORT_PARAMETER= params[1]+"?";
                final String API= "";
                final String URLString= MOVIEDB_BASE_URL+SORT_PARAMETER+"api_key="+API;


                URL url= new URL(URLString);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Reading the inputStream into a string
                InputStream inputStream= urlConnection.getInputStream();
                StringBuffer buffer= new StringBuffer();
                if(inputStream==null)
                {
                    movieJsonStr=null;
                    result=0;
                }
                reader= new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while((line=reader.readLine())!=null)
                {
                    buffer.append(line+"\n");
                }

                if(buffer.length()==0) {
                    movieJsonStr=null;
                    result=0;
                }

                movieJsonStr= buffer.toString();
                Log.d(LOG_TAG, movieJsonStr);
                parseResult(movieJsonStr);
                result=1;

            }
            catch(Exception e)
            {
                Log.d(LOG_TAG, e.getLocalizedMessage());
            }finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            //Download Complete. Time to update the UI

            super.onPostExecute(result);

            if(result==1)
            {
                mGridViewAdapter.setMovieData(movieData);
            }
            else
            {
                Toast.makeText(GridViewActivity.this, "HTTP fetch request failed", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);

        }

        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
    }

    private void parseResult(String result) {
        try
        {
            JSONObject movieObject= new JSONObject(result);
            JSONArray movieList= movieObject.optJSONArray("results");
            GridItem item;
            movieData.clear();
            for(int i=0; i<movieList.length(); i++)
            {
                JSONObject movie= movieList.getJSONObject(i);
                String title= movie.getString("original_title");
                item= new GridItem();
                item.setTitle(title);
                String posterPath= movie.getString("poster_path");
                posterPath= posterPath.replace("\\", "/");
               // Log.d(LOG_TAG, posterPath);
                item.setImage("http://image.tmdb.org/t/p/w185" + posterPath);
                String releaseDate= movie.getString("release_date");
                item.setReleaseDate(releaseDate);
                String synopsis= movie.getString("overview");
                item.setSynopsis(synopsis);
                String averageRating= movie.getString("vote_average");
                item.setAverageRating(averageRating);

                movieData.add(item);

            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();

        }
    }


}
