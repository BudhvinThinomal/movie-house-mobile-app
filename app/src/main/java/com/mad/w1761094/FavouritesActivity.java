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

public class FavouritesActivity extends AppCompatActivity {
    //Declaring the variables & array lists
    private List<MovieModel> allMoviesListFav;
    private List<String> sortedFavMoviesTitles;
    private List<CheckBox> checkBoxesFavList;
    private MovieDatabase movieDatabase;

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        //Referencing to the views
        TextView emptyFavText = findViewById(R.id.favouritesEmptyNotify);
        LinearLayout linearLayoutFav = findViewById(R.id.favouritesLinear);
        TextView blankLineFav;
        CheckBox checkBoxFav;
        //Initialize Array lists
        sortedFavMoviesTitles = new ArrayList<>();
        checkBoxesFavList = new ArrayList<>();


        //Use to access to the MainActivity
        ImageView favouritesBackBtn = findViewById(R.id.favouritePageActionBarImgView);
        favouritesBackBtn.setOnClickListener(view -> {
            emptyFavText.setVisibility(View.INVISIBLE);
            startActivity(new Intent(FavouritesActivity.this, MainActivity.class));
        });

        //Creating object to reference to MovieDatabase class
        movieDatabase = new MovieDatabase(FavouritesActivity.this);
        allMoviesListFav = movieDatabase.loadMovieDetails();

        if (allMoviesListFav.size() == 0) {
            emptyFavText.setVisibility(View.VISIBLE);
        } else {
            //Extract movies titles from object and add to array list
            for (int x = 0; x < allMoviesListFav.size(); x++) {
                if (allMoviesListFav.get(x).getMovieFavourite() == 1) {
                    String movieTitle = String.valueOf(allMoviesListFav.get(x).getMovieTitle());
                    sortedFavMoviesTitles.add(movieTitle);
                }

            }

            if (sortedFavMoviesTitles.size() == 0) {
                emptyFavText.setVisibility(View.VISIBLE);
            } else {
                //Sort according to alphabetical order
                Collections.sort(sortedFavMoviesTitles, String.CASE_INSENSITIVE_ORDER);

                //Create checkbox for each movie title
                for (int i = 0; i < sortedFavMoviesTitles.size(); i++) {
                    checkBoxFav = new CheckBox(FavouritesActivity.this);
                    checkBoxFav.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    checkBoxFav.setBackground(this.getResources().getDrawable(R.drawable.customize_edit_text));
                    checkBoxFav.setText(sortedFavMoviesTitles.get(i));
                    checkBoxesFavList.add(checkBoxFav);
                    checkBoxFav.setTextColor(getResources().getColor(R.color.black));
                    checkBoxFav.setTextSize(18);
                    checkBoxFav.setChecked(true);
                    checkBoxFav.setTypeface(checkBoxFav.getTypeface(), Typeface.BOLD);
                    checkBoxFav.setPadding(40, 0, 30, 0);
                    linearLayoutFav.addView(checkBoxFav);

                    blankLineFav = new TextView(FavouritesActivity.this);
                    blankLineFav.setPadding(0, 5, 0, 5);
                    linearLayoutFav.addView(blankLineFav);
                }
            }
        }
        removeFromFavourite();
    }

    /*
     *Use to remove movies from favourite list
     *Remove movies from favourites by using the checkboxes
     *Movie object update when checkbox blank ticked
     */
    private void removeFromFavourite() {
        Button removeFromFav = findViewById(R.id.removeFromFav);
        removeFromFav.setOnClickListener(view -> {
            if (allMoviesListFav.size() == 0) {
                Toast.makeText(FavouritesActivity.this, R.string.favourites_empty_text, Toast.LENGTH_SHORT).show();
            } else {
                int checkedItem = 0;
                for (int y = 0; y < sortedFavMoviesTitles.size(); y++) {
                    if (!(checkBoxesFavList.get(y).isChecked())) {
                        int count = 0;
                        while (!(sortedFavMoviesTitles.get(y).equals(allMoviesListFav.get(count).getMovieTitle()))) {
                            count++;
                        }
                        allMoviesListFav.get(count).setMovieFavourite(0);

                        movieDatabase.updateDb(allMoviesListFav.get(count).getId(), allMoviesListFav.get(count).getMovieTitle(), allMoviesListFav.get(count).getMovieYear(), allMoviesListFav.get(count).getMovieDirector(), allMoviesListFav.get(count).getMovieCast(), allMoviesListFav.get(count).getMovieRatings(), allMoviesListFav.get(count).getMovieReview(), allMoviesListFav.get(count).getMovieFavourite());

                        checkBoxesFavList.get(y).setEnabled(false);
                        checkBoxesFavList.get(y).setTextColor(getResources().getColor(R.color.red_700));
                        checkBoxesFavList.get(y).setText(R.string.favourites_unselected_check_box);

                        checkedItem++;
                    }
                }

                if (checkedItem == 0) {
                    Toast.makeText(FavouritesActivity.this, R.string.favourites_no_unselected, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FavouritesActivity.this, R.string.favourites_unselected, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}