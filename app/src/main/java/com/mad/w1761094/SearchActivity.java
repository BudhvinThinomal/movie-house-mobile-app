package com.mad.w1761094;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    //Declaring the variables & array list
    private EditText searchValue;
    private ImageButton lookupBtn;
    private List<List<String>> outputValues;
    private TextView emptyResult;
    private LinearLayout linearLayoutSearch;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Referencing to the views
        outputValues = new ArrayList<>();
        searchValue = findViewById(R.id.searchText);
        lookupBtn = findViewById(R.id.searchBtn);
        emptyResult = findViewById(R.id.searchEmptyNotify);
        linearLayoutSearch = findViewById(R.id.searchDetailsLayout);

        //Use to access to the MainActivity
        ImageView searchBackBtn = findViewById(R.id.searchPageActionBarImgView);
        searchBackBtn.setOnClickListener(view -> {
            emptyResult.setVisibility(View.INVISIBLE);
            startActivity(new Intent(SearchActivity.this, MainActivity.class));
        });

        searchDetails();

    }

    /*
     *Use to get value from edit text
     *Send value to database
     */
    private void searchDetails() {
        MovieDatabase movieDatabase = new MovieDatabase(SearchActivity.this);
        lookupBtn.setOnClickListener(view -> {
            emptyResult.setVisibility(View.INVISIBLE);
            linearLayoutSearch.removeAllViews();

            String value = searchValue.getText().toString();

            if (value.equals("")) {
                Toast.makeText(SearchActivity.this, R.string.search_empty_value, Toast.LENGTH_SHORT).show();
            } else {
                outputValues = movieDatabase.searchKey(value);
                results();
            }
        });
    }

    /*
     *Use to display output results
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void results() {
        if ((outputValues.get(0).size() == 0) && (outputValues.get(1).size() == 0) && (outputValues.get(2).size() == 0)) {
            emptyResult.setVisibility(View.VISIBLE);
        } else {
            TextView titleTag = new TextView(SearchActivity.this);
            titleTag.setBackground(this.getResources().getDrawable(R.drawable.customize_notify_text));
            titleTag.setText(R.string.search_movie_title);
            titleTag.setTextColor(getResources().getColor(R.color.blue_850));
            titleTag.setTextSize(22);
            titleTag.setTypeface(titleTag.getTypeface(), Typeface.BOLD);
            linearLayoutSearch.addView(titleTag);

            TextView blankLineSearch = new TextView(SearchActivity.this);
            blankLineSearch.setPadding(0, 0, 0, 5);
            linearLayoutSearch.addView(blankLineSearch);
            for (int i = 0; i < outputValues.get(0).size(); i++) {
                TextView movieTitle = new TextView(SearchActivity.this);
                movieTitle.setBackground(this.getResources().getDrawable(R.drawable.customize_edit_text));
                movieTitle.setText(outputValues.get(0).get(i));
                movieTitle.setTextColor(getResources().getColor(R.color.black));
                movieTitle.setTextSize(18);
                movieTitle.setTypeface(movieTitle.getTypeface(), Typeface.BOLD);
                linearLayoutSearch.addView(movieTitle);

                blankLineSearch = new TextView(SearchActivity.this);
                blankLineSearch.setPadding(0, 0, 0, 2);
                linearLayoutSearch.addView(blankLineSearch);
            }
            blankLineSearch = new TextView(SearchActivity.this);
            blankLineSearch.setPadding(0, 15, 0, 5);
            linearLayoutSearch.addView(blankLineSearch);

            TextView directorTag = new TextView(SearchActivity.this);
            directorTag.setBackground(this.getResources().getDrawable(R.drawable.customize_notify_text));
            directorTag.setText(R.string.search_movie_director);
            directorTag.setTextColor(getResources().getColor(R.color.blue_850));
            directorTag.setTextSize(22);
            directorTag.setTypeface(directorTag.getTypeface(), Typeface.BOLD);
            linearLayoutSearch.addView(directorTag);

            blankLineSearch = new TextView(SearchActivity.this);
            blankLineSearch.setPadding(0, 0, 0, 5);
            linearLayoutSearch.addView(blankLineSearch);
            for (int i = 0; i < outputValues.get(1).size(); i++) {
                TextView movieDirector = new TextView(SearchActivity.this);
                movieDirector.setBackground(this.getResources().getDrawable(R.drawable.customize_edit_text));
                movieDirector.setText(outputValues.get(1).get(i));
                movieDirector.setTextColor(getResources().getColor(R.color.black));
                movieDirector.setTextSize(18);
                movieDirector.setTypeface(movieDirector.getTypeface(), Typeface.BOLD);
                linearLayoutSearch.addView(movieDirector);

                blankLineSearch = new TextView(SearchActivity.this);
                blankLineSearch.setPadding(0, 0, 0, 2);
                linearLayoutSearch.addView(blankLineSearch);
            }
            blankLineSearch = new TextView(SearchActivity.this);
            blankLineSearch.setPadding(0, 15, 0, 5);
            linearLayoutSearch.addView(blankLineSearch);

            TextView castTag = new TextView(SearchActivity.this);
            castTag.setBackground(this.getResources().getDrawable(R.drawable.customize_notify_text));
            castTag.setText(R.string.search_movie_cast);
            castTag.setTextColor(getResources().getColor(R.color.blue_850));
            castTag.setTextSize(22);
            castTag.setTypeface(castTag.getTypeface(), Typeface.BOLD);
            linearLayoutSearch.addView(castTag);

            blankLineSearch = new TextView(SearchActivity.this);
            blankLineSearch.setPadding(0, 0, 0, 2);
            linearLayoutSearch.addView(blankLineSearch);
            for (int i = 0; i < outputValues.get(2).size(); i++) {
                TextView movieCast = new TextView(SearchActivity.this);
                movieCast.setBackground(this.getResources().getDrawable(R.drawable.customize_edit_text));
                movieCast.setText(outputValues.get(2).get(i));
                movieCast.setTextColor(getResources().getColor(R.color.black));
                movieCast.setTextSize(18);
                movieCast.setTypeface(movieCast.getTypeface(), Typeface.BOLD);
                linearLayoutSearch.addView(movieCast);

                blankLineSearch = new TextView(SearchActivity.this);
                blankLineSearch.setPadding(0, 0, 0, 5);
                linearLayoutSearch.addView(blankLineSearch);
            }
            blankLineSearch = new TextView(SearchActivity.this);
            blankLineSearch.setPadding(0, 15, 0, 5);
            linearLayoutSearch.addView(blankLineSearch);
        }
    }

}