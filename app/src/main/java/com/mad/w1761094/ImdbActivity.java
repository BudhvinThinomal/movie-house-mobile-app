package com.mad.w1761094;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ImdbActivity extends AppCompatActivity {
    //Declaring the variables & array lists
    private static final String LOG_TAG = ImdbActivity.class.getSimpleName();
    private String baseMovieUrl;
    private String ratingBaseUrl;
    private String downloadImgUrl;
    private ArrayList<String> movieIdList;
    private ArrayList<String> movieTitleList;
    private ArrayList<String> movieImageList;
    private ArrayList<String> movieRatingList;
    private int count;
    private LinearLayout linearLayoutImdb;
    private TextView emptyImdbText;
    private ArrayList<ImageView> movieImgViewList;
    private int movieImgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_m_d_b);

        //Referencing to the views
        emptyImdbText = findViewById(R.id.imdbEmptyNotify);
        linearLayoutImdb = findViewById(R.id.imdbBtnLinear);
        movieIdList = new ArrayList<>();
        movieTitleList = new ArrayList<>();
        movieImageList = new ArrayList<>();
        movieRatingList = new ArrayList<>();
        movieImgViewList = new ArrayList<>();
        count = 0;


        //Use to access to the RatingsActivity
        ImageView selectBackBtn = findViewById(R.id.imdbPageActionBarImgView);
        selectBackBtn.setOnClickListener(view -> {
            emptyImdbText.setVisibility(View.INVISIBLE);
            startActivity(new Intent(ImdbActivity.this, RatingsActivity.class));
        });

        //Get data from RatingsActivity using intent
        Intent imdbIntent = getIntent();
        ArrayList<String> receivedMovieDetails = imdbIntent.getStringArrayListExtra("send_details");

        //Extract movie title from receivedMovieDetails array list
        String intentMovieTitle = receivedMovieDetails.get(0);

        if (!isInternetConnected()) {
            Toast toast = Toast.makeText(ImdbActivity.this, "Internet is not connected", Toast.LENGTH_LONG);
            toast.show();
        } else {
            baseMovieUrl = "https://imdb-api.com/en/API/SearchTitle/k_e3u52ea9/" + intentMovieTitle;
            ratingBaseUrl = "https://imdb-api.com/en/API/UserRatings/k_e3u52ea9/";

            new MovieList().execute();
        }
    }

    //Check the connection availability
    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(LOG_TAG, "Internet is connected");
            return true;
        } else {
            Log.d(LOG_TAG, "Internet is not connected");
            return false;
        }
    }

    //Use to display selected movie rating from IMDB
    @SuppressLint("UseCompatLoadingForDrawables")
    private void guiDisplay() {
        if (movieIdList.size() == 0) {
            emptyImdbText.setVisibility(View.VISIBLE);
        } else {
            //Create buttons for each movie title
            for (int i = 0; i < movieIdList.size(); i++) {
                Button imdbBtn = new Button(ImdbActivity.this);
                imdbBtn.setBackground(this.getResources().getDrawable(R.drawable.customize_edit_text));
                imdbBtn.setText(movieTitleList.get(i));
                imdbBtn.setTextColor(getResources().getColor(R.color.black));
                imdbBtn.setTextSize(18);
                imdbBtn.setId(i);
                imdbBtn.setTypeface(imdbBtn.getTypeface(), Typeface.BOLD);
                linearLayoutImdb.addView(imdbBtn);

                TextView blankLineImdb = new TextView(ImdbActivity.this);
                linearLayoutImdb.addView(blankLineImdb);

                TextView ratingTextView = new TextView(ImdbActivity.this);
                ratingTextView.setBackground(this.getResources().getDrawable(R.drawable.customize_heading));
                String ratingDisplay;
                if (movieRatingList.get(i).equals("null") || movieRatingList.get(i).equals("")) {
                    ratingDisplay = " Rating : Total rating not available";
                } else {
                    ratingDisplay = " Rating : " + movieRatingList.get(i);
                }
                ratingTextView.setText(ratingDisplay);
                ratingTextView.setTextColor(getResources().getColor(R.color.black));
                ratingTextView.setTextSize(18);
                ratingTextView.setPadding(10, 2, 10, 2);
                ratingTextView.setTypeface(ratingTextView.getTypeface(), Typeface.BOLD);
                linearLayoutImdb.addView(ratingTextView);

                ImageView movieImg = new ImageView(ImdbActivity.this);
                movieImg.setMaxWidth(ratingTextView.getMaxWidth());
                movieImgViewList.add(movieImg);
                linearLayoutImdb.addView(movieImg);

                blankLineImdb = new TextView(ImdbActivity.this);
                blankLineImdb.setPadding(0, 6, 0, 6);
                linearLayoutImdb.addView(blankLineImdb);

                imdbBtn.setOnClickListener(getMovieImage(imdbBtn));
            }
        }
    }

    View.OnClickListener getMovieImage(Button button) {
        return view -> {
            downloadImgUrl = movieImageList.get(button.getId());
            movieImgId = button.getId();
            new MovieImage().execute();
        };
    }

    /*
     *Create AsyncTask class for extract movie list from jason file
     * Movie id, title and image url added to array lists
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class MovieList extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Log.d(LOG_TAG, "MovieList doInBackground()");
                URL url = new URL(baseMovieUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String result = bufferedReader.readLine();
                Log.d(LOG_TAG, "Search movie title results : " + result);

                httpURLConnection.disconnect();
                bufferedReader.close();

                return result;
            } catch (Exception e) {
                Log.d(LOG_TAG, "Error in MovieList doInBackground");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject mainJasonData = new JSONObject(s); //s is the string that is returned from doInBackGround
                JSONArray jasonArrayData = mainJasonData.getJSONArray("results");

                String tempId;
                String tempTitle;
                String tempImage;

                for (int x = 0; x < jasonArrayData.length(); x++) {
                    try {
                        JSONObject index = jasonArrayData.getJSONObject(x);
                        tempId = index.getString("id");
                        tempTitle = index.getString("title");
                        tempImage = index.getString("image");
                    } catch (Exception e) {
                        JSONObject index = jasonArrayData.getJSONObject(x);
                        tempId = index.getString("id");
                        tempTitle = index.getString("title");
                        tempImage = index.getString("image");
                    }

                    movieIdList.add(tempId);
                    movieTitleList.add(tempTitle);
                    movieImageList.add(tempImage);
                }

                Log.d(LOG_TAG, "Search movie id list : " + movieIdList.toString());
                Log.d(LOG_TAG, "Search movie title list : " + movieTitleList.toString());
                Log.d(LOG_TAG, "Search movie image list : " + movieImageList.toString());

                for (int y = 0; y < movieIdList.size(); y++) {
                    new GetMovieRating().execute();
                }
            } catch (Exception e) {
                Log.d(LOG_TAG, "MovieList onPostExecute: Error");
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    /*
     *Create AsyncTask class for extract movie rating from jason file
     * Movie rating added to array list
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class GetMovieRating extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String ratingMovieUrl = ratingBaseUrl + movieIdList.get(count);
                Log.d(LOG_TAG, "GetMovieRating doInBackground()");
                URL rateUrl = new URL(ratingMovieUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) rateUrl.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String ratingResult = bufferedReader.readLine();
                Log.d(LOG_TAG, "Search movie rating results : " + ratingResult);

                httpURLConnection.disconnect();
                bufferedReader.close();

                count++;
                return ratingResult;
            } catch (Exception e) {
                Log.d(LOG_TAG, "Error in GetMovieRating doInBackground");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject mainJasonData = new JSONObject(s);

                String rating;
                try {
                    rating = String.valueOf(mainJasonData.get("totalRating"));
                } catch (Exception e) {
                    rating = String.valueOf(mainJasonData.get("totalRating"));
                }

                movieRatingList.add(rating);
                Log.d(LOG_TAG, "Search movie rating list : " + movieRatingList);
                if (movieIdList.size() == movieRatingList.size()) {
                    guiDisplay();
                }
            } catch (Exception e) {
                Log.d(LOG_TAG, "GetMovieRating onPostExecute: Error");
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    /*
     *Create AsyncTask class for load image
     * Movie images added to array list
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class MovieImage extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL imgUrl = new URL(downloadImgUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) imgUrl.openConnection();
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                httpURLConnection.disconnect();
                return bitmap;
            } catch (MalformedURLException e) {
                Log.d(LOG_TAG, "Error in MovieImage doInBackground");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(LOG_TAG, "Error in MovieImage doInBackground");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                movieImgViewList.get(movieImgId).getLayoutParams().height = 1500;
                movieImgViewList.get(movieImgId).setImageBitmap(bitmap);
                Toast.makeText(ImdbActivity.this, "Image Load successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ImdbActivity.this, "Fail to load image", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

}