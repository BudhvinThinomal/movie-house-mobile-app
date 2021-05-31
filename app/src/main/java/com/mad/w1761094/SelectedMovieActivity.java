package com.mad.w1761094;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SelectedMovieActivity extends AppCompatActivity {
    //Declaring the variables
    private EditText movieTitle, movieYear, movieDirector, movieCast, movieReview;
    private RatingBar movieRating;
    private RadioButton favRadio;
    private TextView favouriteStatus;
    private Button updateBtn;
    private int id;
    private int movieRatingsValue;
    private String favStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_movie);

        //Referencing to the views
        movieTitle = findViewById(R.id.selectedTextTitle);
        movieYear = findViewById(R.id.selectedTextYear);
        movieDirector = findViewById(R.id.selectedTextDirector);
        movieCast = findViewById(R.id.selectedTextCast);
        movieRating = findViewById(R.id.selectedRatingBar);
        movieReview = findViewById(R.id.selectedTextReview);
        favouriteStatus = findViewById(R.id.selectedMovieFavStatus);
        favRadio = findViewById(R.id.favouriteRadioBtn);
        RadioButton nonFavRadio = findViewById(R.id.nonFavouriteRadioBtn);
        updateBtn = findViewById(R.id.selectedUpdateBtn);

        //Use to access to the EditMoviesActivity
        ImageView selectBackBtn = findViewById(R.id.selectPageActionBarImgView);
        selectBackBtn.setOnClickListener(view -> startActivity(new Intent(SelectedMovieActivity.this, EditMoviesActivity.class)));

        //Get data from EditMoviesActivity using intent
        Intent selectIntent = getIntent();
        ArrayList<String> receivedMovieDetails = selectIntent.getStringArrayListExtra("send_details");

        //Referencing to the views
        id = Integer.parseInt(receivedMovieDetails.get(0));
        String movieTitleValue = receivedMovieDetails.get(1);
        int movieYearValue = Integer.parseInt(receivedMovieDetails.get(2));
        String movieDirectorValue = receivedMovieDetails.get(3);
        String movieCastValue = receivedMovieDetails.get(4);
        movieRatingsValue = Integer.parseInt(receivedMovieDetails.get(5));
        String movieReviewValue = receivedMovieDetails.get(6);
        favStatus = receivedMovieDetails.get(7);


        movieTitle.setText(movieTitleValue);
        movieYear.setText(String.valueOf(movieYearValue));
        movieDirector.setText(movieDirectorValue);
        movieCast.setText(movieCastValue);
        movieRating.setRating(movieRatingsValue);
        movieReview.setText(movieReviewValue);
        favouriteStatus.setText(favStatus);

        if (favStatus.equals("Favourite")) {
            favRadio.setChecked(true);
        } else {
            nonFavRadio.setChecked(true);
        }

        updateDatabase();
    }


    /*
     *Use to update movie details
     *Object values access using getters
     *Changed values assign to variables
     * Send data to database for update movie table
     */
    private void updateDatabase() {
        updateBtn.setOnClickListener(view -> {

            movieRatingsValue = (int) movieRating.getRating();
            int favour;
            if ((!movieTitle.getText().toString().equals("")) && (!movieYear.getText().toString().equals("")) && (!movieDirector.getText().toString().equals("")) && (!movieCast.getText().toString().equals("")) && (!(movieRatingsValue == 0)) && (!movieReview.getText().toString().equals("")) && (Integer.parseInt(movieYear.getText().toString()) > 1895) && (movieRatingsValue > 0) && (movieRatingsValue <= 10)) {
                if (favRadio.isChecked()) {
                    favStatus = "Favourite";
                    favour = 1;
                } else {
                    favStatus = "Not Favourite";
                    favour = 0;
                }
                favouriteStatus.setText(favStatus);

                MovieDatabase movieDatabase = new MovieDatabase(SelectedMovieActivity.this);
                movieDatabase.updateDb(id, movieTitle.getText().toString(), Integer.parseInt(movieYear.getText().toString()), movieDirector.getText().toString(), movieCast.getText().toString(), movieRatingsValue, movieReview.getText().toString(), favour);

                Toast.makeText(SelectedMovieActivity.this, R.string.selected_update, Toast.LENGTH_SHORT).show();

            } else if ((!movieYear.getText().toString().equals("")) && (Integer.parseInt(movieYear.getText().toString()) <= 1895)) {
                Toast.makeText(SelectedMovieActivity.this, R.string.register_year_notify, Toast.LENGTH_SHORT).show();
            } else if (!(movieRatingsValue == 0) && ((movieRatingsValue <= 0) || (movieRatingsValue > 10))) {
                Toast.makeText(SelectedMovieActivity.this, R.string.register_rating_notify, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SelectedMovieActivity.this, R.string.register_empty_notify, Toast.LENGTH_SHORT).show();
            }
        });

    }
}