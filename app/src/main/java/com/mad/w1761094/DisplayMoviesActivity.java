package com.mad.w1761094;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DisplayMoviesActivity extends AppCompatActivity {
    //Declaring the variables & array lists
    private List<MovieModel> allMoviesList;
    private List<String> sortedMovieTitles;
    private List<CheckBox> checkBoxesList;
    private MovieDatabase movieDatabase;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movies);

        //Referencing to the views
        TextView emptyListText = findViewById(R.id.displayEmptyNotify);
        LinearLayout linearLayout = findViewById(R.id.displayLinear);
        CheckBox checkBox;
        TextView blankLine;
        //Initialize Array lists
        sortedMovieTitles = new ArrayList<>();
        checkBoxesList = new ArrayList<>();

        //Use to access to the MainActivity
        ImageView displayBackBtn = findViewById(R.id.displayPageActionBarImgView);
        displayBackBtn.setOnClickListener(view -> {
            emptyListText.setVisibility(View.INVISIBLE);
            startActivity(new Intent(DisplayMoviesActivity.this, MainActivity.class));
        });

        //Creating object to reference to MovieDatabase class
        movieDatabase = new MovieDatabase(DisplayMoviesActivity.this);
        allMoviesList = movieDatabase.loadMovieDetails();           //Loading data from Database

        if (allMoviesList.size() == 0) {
            emptyListText.setVisibility(View.VISIBLE);
        } else {
            //Extract movies titles from object and add to array list
            for (int x = 0; x < allMoviesList.size(); x++) {
                String movieTitle = String.valueOf(allMoviesList.get(x).getMovieTitle());
                sortedMovieTitles.add(movieTitle);
            }
            //Sort according to alphabetical order
            Collections.sort(sortedMovieTitles, String.CASE_INSENSITIVE_ORDER);

            //Create checkbox for each movie title
            for (int i = 0; i < sortedMovieTitles.size(); i++) {
                checkBox = new CheckBox(DisplayMoviesActivity.this);
                checkBox.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                checkBox.setBackground(this.getResources().getDrawable(R.drawable.customize_edit_text));
                checkBox.setText(sortedMovieTitles.get(i));
                checkBoxesList.add(checkBox);
                checkBox.setTextColor(getResources().getColor(R.color.black));
                checkBox.setTextSize(18);
                checkBox.setTypeface(checkBox.getTypeface(), Typeface.BOLD);
                checkBox.setPadding(40, 0, 30, 0);
                linearLayout.addView(checkBox);

                blankLine = new TextView(DisplayMoviesActivity.this);
                blankLine.setPadding(0, 5, 0, 5);
                linearLayout.addView(blankLine);
            }
        }
        addToFavourite();
    }

    /*
     *Use to add movies to favourite list
     *Add movies as favourites by using the checkboxes
     *Movie object update when checkbox ticked
     */
    private void addToFavourite() {
        Button addToFav = findViewById(R.id.displayAddToFav);
        addToFav.setOnClickListener(view -> {
            if (allMoviesList.size() == 0) {
                Toast.makeText(DisplayMoviesActivity.this, R.string.display_empty_text, Toast.LENGTH_SHORT).show();
            } else {
                int checkedItem = 0;
                for (int y = 0; y < sortedMovieTitles.size(); y++) {
                    if (checkBoxesList.get(y).isChecked()) {
                        int count = 0;
                        while (!(sortedMovieTitles.get(y).equals(allMoviesList.get(count).getMovieTitle()))) {
                            count++;
                        }
                        allMoviesList.get(count).setMovieFavourite(1);

                        movieDatabase.updateDb(allMoviesList.get(count).getId(), allMoviesList.get(count).getMovieTitle(), allMoviesList.get(count).getMovieYear(), allMoviesList.get(count).getMovieDirector(), allMoviesList.get(count).getMovieCast(), allMoviesList.get(count).getMovieRatings(), allMoviesList.get(count).getMovieReview(), allMoviesList.get(count).getMovieFavourite());

                        checkedItem++;
                    }

                }

                if (checkedItem == 0) {
                    Toast.makeText(DisplayMoviesActivity.this, R.string.display_no_selected, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DisplayMoviesActivity.this, R.string.display_selected, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

