package com.mad.w1761094;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RatingsActivity extends AppCompatActivity {
    //Declaring the array lists
    private List<MovieModel> ratingAllMoviesList;
    private List<RadioButton> radioButtonList;
    private List<String> ratingSortedMovieTitles;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        //Referencing to the views
        TextView emptyRatingText = findViewById(R.id.ratingEmptyNotify);
        RadioGroup radioBtnGroup = findViewById(R.id.ratingGroup);
        TextView blankLineRating;
        RadioButton buttonRating;
        //Initialize Array lists
        ratingSortedMovieTitles = new ArrayList<>();
        radioButtonList = new ArrayList<>();

        //Use to access to the MainActivity
        ImageView ratingsBackBtn = findViewById(R.id.ratingsPageActionBarImgView);
        ratingsBackBtn.setOnClickListener(view -> {
            emptyRatingText.setVisibility(View.INVISIBLE);
            startActivity(new Intent(RatingsActivity.this, MainActivity.class));
        });

        //Creating object to reference to MovieDatabase class
        MovieDatabase movieDatabase = new MovieDatabase(RatingsActivity.this);
        //Declaring the array lists
        ratingAllMoviesList = movieDatabase.loadMovieDetails();

        if (ratingAllMoviesList.size() == 0) {
            emptyRatingText.setVisibility(View.VISIBLE);
        } else {
            //Extract movies titles from object and add to array list
            for (int x = 0; x < ratingAllMoviesList.size(); x++) {
                String movieTitle = String.valueOf(ratingAllMoviesList.get(x).getMovieTitle());
                ratingSortedMovieTitles.add(movieTitle);
            }
            //Sort according to alphabetical order
            Collections.sort(ratingSortedMovieTitles, String.CASE_INSENSITIVE_ORDER);

            //Create radio buttons for each movie title
            for (int i = 0; i < ratingSortedMovieTitles.size(); i++) {
                buttonRating = new RadioButton(RatingsActivity.this);
                buttonRating.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                radioButtonList.add(buttonRating);
                buttonRating.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                buttonRating.setBackground(this.getResources().getDrawable(R.drawable.customize_edit_text));
                buttonRating.setText(ratingSortedMovieTitles.get(i));
                buttonRating.setTextColor(getResources().getColor(R.color.black));
                buttonRating.setTextSize(18);
                buttonRating.setTypeface(buttonRating.getTypeface(), Typeface.BOLD);
                radioBtnGroup.addView(buttonRating);

                blankLineRating = new TextView(RatingsActivity.this);
                blankLineRating.setPadding(0, 5, 0, 5);
                radioBtnGroup.addView(blankLineRating);
            }
        }
        selectCheckedItem();
    }

    /*
     *Use to select movie to find IMDB rating
     *Select movie using radio buttons
     *Send selected movie details to the IMDB activity
     */
    private void selectCheckedItem() {
        Button findRatingBtn = findViewById(R.id.findMovieBtn);
        List<String> selectedMovie = new ArrayList<>();
        findRatingBtn.setOnClickListener(view -> {
            if (ratingAllMoviesList.size() == 0) {
                Toast.makeText(RatingsActivity.this, R.string.display_empty_text, Toast.LENGTH_SHORT).show();
            } else {
                Intent imdbIntent = new Intent(RatingsActivity.this, ImdbActivity.class);
                int checkedItem = 0;
                for (int x = 0; x < ratingSortedMovieTitles.size(); x++) {
                    if (radioButtonList.get(x).isChecked()) {
                        int count = 0;
                        while (!(ratingSortedMovieTitles.get(x).equals(ratingAllMoviesList.get(count).getMovieTitle()))) {
                            count++;
                        }
                        selectedMovie.add(ratingAllMoviesList.get(count).getMovieTitle());
                        checkedItem++;
                        break;
                    }
                }

                if (checkedItem == 0) {
                    Toast.makeText(RatingsActivity.this, R.string.ratings_no_selected, Toast.LENGTH_SHORT).show();
                } else {
                    imdbIntent.putExtra("send_details", (Serializable) selectedMovie);
                    startActivity(imdbIntent);
                }
            }
        });

    }

}