package com.mad.w1761094;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditMoviesActivity extends AppCompatActivity {
    //Declaring the array list
    private List<MovieModel> editAllMoviesList;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movies);

        //Referencing to the views
        TextView emptyEditText = findViewById(R.id.editEmptyNotify);
        LinearLayout linearLayoutEdit = findViewById(R.id.editLinear);
        TextView blankLineEdit;
        Button buttonEdit;
        List<String> editSortedMovieTitles = new ArrayList<>();

        //Use to access to the MainActivity
        ImageView editBackBtn = findViewById(R.id.editPageActionBarImgView);
        editBackBtn.setOnClickListener(view -> {
            emptyEditText.setVisibility(View.INVISIBLE);
            startActivity(new Intent(EditMoviesActivity.this, MainActivity.class));
        });

        //Creating object to reference to MovieDatabase class
        MovieDatabase movieDatabase = new MovieDatabase(EditMoviesActivity.this);
        editAllMoviesList = movieDatabase.loadMovieDetails();

        if (editAllMoviesList.size() == 0) {
            emptyEditText.setVisibility(View.VISIBLE);
        } else {
            //Extract movies titles from object and add to array list
            for (int x = 0; x < editAllMoviesList.size(); x++) {
                String movieTitle = String.valueOf(editAllMoviesList.get(x).getMovieTitle());
                editSortedMovieTitles.add(movieTitle);
            }
            //Sort according to alphabetical order
            Collections.sort(editSortedMovieTitles, String.CASE_INSENSITIVE_ORDER);

            //Create buttons for each movie title
            for (int i = 0; i < editSortedMovieTitles.size(); i++) {
                buttonEdit = new Button(EditMoviesActivity.this);
                buttonEdit.setBackground(this.getResources().getDrawable(R.drawable.customize_edit_text));
                buttonEdit.setText(editSortedMovieTitles.get(i));
                buttonEdit.setTextColor(getResources().getColor(R.color.black));
                buttonEdit.setTextSize(18);
                buttonEdit.setTypeface(buttonEdit.getTypeface(), Typeface.BOLD);
                linearLayoutEdit.addView(buttonEdit);

                blankLineEdit = new TextView(EditMoviesActivity.this);
                blankLineEdit.setPadding(0, 5, 0, 5);
                linearLayoutEdit.addView(blankLineEdit);

                buttonEdit.setOnClickListener(updateMovieDetails(buttonEdit));
            }
        }


    }

    /*
     *Use to access to movie details
     *Add movie details to array list and send to SelectMovieActivity using intent
     */
    View.OnClickListener updateMovieDetails(Button button) {
        List<String> selectedMovie = new ArrayList<>();
        return view -> {
            Intent selectedIntent = new Intent(EditMoviesActivity.this, SelectedMovieActivity.class);

            for (int i = 0; i < editAllMoviesList.size(); i++) {
                if (button.getText().toString().equalsIgnoreCase(editAllMoviesList.get(i).getMovieTitle())) {

                    int id = editAllMoviesList.get(i).getId();
                    String movieTitle = editAllMoviesList.get(i).getMovieTitle();
                    int movieYear = editAllMoviesList.get(i).getMovieYear();
                    String movieDirector = editAllMoviesList.get(i).getMovieDirector();
                    String movieCast = editAllMoviesList.get(i).getMovieCast();
                    int movieRatings = editAllMoviesList.get(i).getMovieRatings();
                    String movieReview = editAllMoviesList.get(i).getMovieReview();
                    String favStatus;

                    if (editAllMoviesList.get(i).getMovieFavourite() == 0) {
                        favStatus = "Not Favourite";
                    } else {
                        favStatus = "Favourite";
                    }
                    selectedMovie.add(String.valueOf(id));
                    selectedMovie.add(movieTitle);
                    selectedMovie.add(String.valueOf(movieYear));
                    selectedMovie.add(movieDirector);
                    selectedMovie.add(movieCast);
                    selectedMovie.add(String.valueOf(movieRatings));
                    selectedMovie.add(movieReview);
                    selectedMovie.add(favStatus);

                    selectedIntent.putExtra("send_details", (Serializable) selectedMovie);
                    startActivity(selectedIntent);
                }
            }
        };
    }
}