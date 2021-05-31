package com.mad.w1761094;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterMovieActivity extends AppCompatActivity {
    //Declaring the variables
    private EditText movieTitle, movieYear, movieDirector, movieCast, movieRatings, movieReview;
    private Button saveToDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);

        //Referencing to the views
        movieTitle = findViewById(R.id.editTextTitle);
        movieYear = findViewById(R.id.editTextYear);
        movieDirector = findViewById(R.id.editTextDirector);
        movieCast = findViewById(R.id.editTextAct);
        movieRatings = findViewById(R.id.editTextRating);
        movieReview = findViewById(R.id.editTextReview);

        saveToDb = findViewById(R.id.registerSaveBtn);

        //Use to access to the MainActivity
        ImageView registerBackBtn = findViewById(R.id.registerPageActionBarImgView);
        registerBackBtn.setOnClickListener(view -> startActivity(new Intent(RegisterMovieActivity.this, MainActivity.class)));

        saveMovieDetails();
    }

    /*
     *Use to add movies to database
     *Add movie details to object
     *Send object to database
     */
    private void saveMovieDetails() {
        saveToDb.setOnClickListener(view -> {
            MovieModel movieModel;

            if ((!movieTitle.getText().toString().equals("")) && (!movieYear.getText().toString().equals("")) && (!movieDirector.getText().toString().equals("")) && (!movieCast.getText().toString().equals("")) && (!movieRatings.getText().toString().equals("")) && (!movieReview.getText().toString().equals("")) && (Integer.parseInt(movieYear.getText().toString()) > 1895) && (Integer.parseInt(movieRatings.getText().toString()) > 0) && (Integer.parseInt(movieRatings.getText().toString()) <= 10)) {
                movieModel = new MovieModel(-1, movieTitle.getText().toString(), Integer.parseInt(movieYear.getText().toString()), movieDirector.getText().toString(), movieCast.getText().toString(), Integer.parseInt(movieRatings.getText().toString()), movieReview.getText().toString(), 0);

                MovieDatabase movieDatabase = new MovieDatabase(RegisterMovieActivity.this);
                movieDatabase.saveDb(movieModel);
                Toast.makeText(RegisterMovieActivity.this, R.string.register_save_successfully, Toast.LENGTH_SHORT).show();
                movieTitle.getText().clear();
                movieYear.getText().clear();
                movieDirector.getText().clear();
                movieCast.getText().clear();
                movieRatings.getText().clear();
                movieReview.getText().clear();

            } else if ((!movieYear.getText().toString().equals("")) && (Integer.parseInt(movieYear.getText().toString()) <= 1895)) {
                Toast.makeText(RegisterMovieActivity.this, R.string.register_year_notify, Toast.LENGTH_SHORT).show();
            } else if ((!movieRatings.getText().toString().equals("")) && ((Integer.parseInt(movieRatings.getText().toString()) <= 0) || (Integer.parseInt(movieRatings.getText().toString()) > 10))) {
                Toast.makeText(RegisterMovieActivity.this, R.string.register_rating_notify, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterMovieActivity.this, R.string.register_empty_notify, Toast.LENGTH_SHORT).show();
            }
        });
    }
}