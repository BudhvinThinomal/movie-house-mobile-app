package com.mad.w1761094;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Use to access to the RegisterMovieActivity
        Button registerMovieBtn = findViewById(R.id.registerBtn);
        registerMovieBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, RegisterMovieActivity.class)));

        //Use to access to the DisplayMoviesActivity
        Button displayMoviesBtn = findViewById(R.id.displayBtn);
        displayMoviesBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, DisplayMoviesActivity.class)));

        //Use to access to the FavouritesActivity
        Button favouritesBtn = findViewById(R.id.favouriteBtn);
        favouritesBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, FavouritesActivity.class)));

        //Use to access to the EditMoviesActivity
        Button editMoviesBtn = findViewById(R.id.editBtn);
        editMoviesBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, EditMoviesActivity.class)));

        //Use to access to the RegisterMovieActivity
        Button searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SearchActivity.class)));

        //Use to access to the RatingsActivity
        Button ratingsBtn = findViewById(R.id.ratingBtn);
        ratingsBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, RatingsActivity.class)));
    }


}