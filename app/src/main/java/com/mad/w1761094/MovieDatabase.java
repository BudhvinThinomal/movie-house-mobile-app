package com.mad.w1761094;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MovieDatabase extends SQLiteOpenHelper {
    //Declaring the constant variables
    private static final String DB_NAME = "movies_database.db";
    private static final String MOVIE_TABLE = "MOVIE_TABLE";
    private static final String COLUMN_MOVIE_ID = "MOVIE_ID";
    private static final String COLUMN_MOVIE_TITLE = "MOVIE_TITLE";
    private static final String COLUMN_MOVIE_YEAR = "MOVIE_YEAR";
    private static final String COLUMN_MOVIE_DIRECTOR = "MOVIE_DIRECTOR";
    private static final String COLUMN_MOVIE_CAST = "MOVIE_CAST";
    private static final String COLUMN_MOVIE_RATING = "MOVIE_RATING";
    private static final String COLUMN_MOVIE_REVIEW = "MOVIE_REVIEW";
    private static final String COLUMN_MOVIE_FAVOURITE = "MOVIE_FAVOURITE";

    //Args constructor
    public MovieDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    /*
     *Use to create a table or override a table
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableState = "CREATE TABLE " + MOVIE_TABLE + " (" + COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_MOVIE_TITLE + " TEXT, " + COLUMN_MOVIE_YEAR + " INT, " + COLUMN_MOVIE_DIRECTOR + " TEXT, " + COLUMN_MOVIE_CAST + " TEXT, " + COLUMN_MOVIE_RATING + " INT, " + COLUMN_MOVIE_REVIEW + " TEXT, " + COLUMN_MOVIE_FAVOURITE + " INT);";
        sqLiteDatabase.execSQL(createTableState);
        Log.i("Database", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    /*
     *Use to save movie details to database
     *Data insert to all the columns using getters
     */
    public void saveDb(MovieModel movieModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_MOVIE_TITLE, movieModel.getMovieTitle());
        contentValues.put(COLUMN_MOVIE_YEAR, movieModel.getMovieYear());
        contentValues.put(COLUMN_MOVIE_DIRECTOR, movieModel.getMovieDirector());
        contentValues.put(COLUMN_MOVIE_CAST, movieModel.getMovieCast());
        contentValues.put(COLUMN_MOVIE_RATING, movieModel.getMovieRatings());
        contentValues.put(COLUMN_MOVIE_REVIEW, movieModel.getMovieReview());
        contentValues.put(COLUMN_MOVIE_FAVOURITE, movieModel.getMovieFavourite());

        database.insert(MOVIE_TABLE, null, contentValues);

    }

    /*
     *Use to load movie details from database
     *Data added to the array list as objects and return array list
     */
    public List<MovieModel> loadMovieDetails() {
        List<MovieModel> movieDetailsList = new ArrayList<>();

        String loadDetailsState = "SELECT * FROM " + MOVIE_TABLE;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(loadDetailsState, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String movieTitle = cursor.getString(1);
                int movieYear = cursor.getInt(2);
                String movieDirector = cursor.getString(3);
                String movieCast = cursor.getString(4);
                int movieRatings = cursor.getInt(5);
                String movieReview = cursor.getString(6);
                int movieFavourite = cursor.getInt(7);
                MovieModel movieModel = new MovieModel(id, movieTitle, movieYear, movieDirector, movieCast, movieRatings, movieReview, movieFavourite);
                movieDetailsList.add(movieModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return movieDetailsList;
    }

    /*
     *Use to update movie details to the database
     *Data insert to all the columns using getters
     *Table update to according to the movie id
     */
    public void updateDb(int id, String movieTitle, int movieYear, String movieDirector, String movieCast, int movieRatings, String movieReview, int movieFavourite) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_MOVIE_ID, id);
        contentValues.put(COLUMN_MOVIE_TITLE, movieTitle);
        contentValues.put(COLUMN_MOVIE_YEAR, movieYear);
        contentValues.put(COLUMN_MOVIE_DIRECTOR, movieDirector);
        contentValues.put(COLUMN_MOVIE_CAST, movieCast);
        contentValues.put(COLUMN_MOVIE_RATING, movieRatings);
        contentValues.put(COLUMN_MOVIE_REVIEW, movieReview);
        contentValues.put(COLUMN_MOVIE_FAVOURITE, movieFavourite);

        database.update(MOVIE_TABLE, contentValues, "MOVIE_ID = ?", new String[]{String.valueOf(id)});
    }

    /*
     *Use to search data from database
     *Values search using three table columns
     *Each column have unique array list to store values
     *Three array list are added to main array list and return main array list
     */
    @SuppressLint("Recycle")
    public List<List<String>> searchKey(String value) {
        SQLiteDatabase database = this.getReadableDatabase();

        List<List<String>> combineArrayList = new ArrayList<>();
        List<String> titleArrayList = new ArrayList<>();
        List<String> directorArrayList = new ArrayList<>();
        List<String> castArrayList = new ArrayList<>();

        String movieTitle = "SELECT * FROM " + MOVIE_TABLE + " WHERE " + COLUMN_MOVIE_TITLE + " LIKE '%" + value + "%'";
        String movieDirector = "SELECT * FROM " + MOVIE_TABLE + " WHERE " + COLUMN_MOVIE_DIRECTOR + " LIKE '%" + value + "%'";
        String movieCast = "SELECT * FROM " + MOVIE_TABLE + " WHERE " + COLUMN_MOVIE_CAST + " LIKE '%" + value + "%'";

        Cursor cursor = database.rawQuery(movieTitle, null);
        String mTitle;
        while (cursor.moveToNext()) {
            mTitle = cursor.getString(1);
            titleArrayList.add(mTitle);
        }

        cursor = database.rawQuery(movieDirector, null);
        String mDirector;
        String directorTitle;
        while (cursor.moveToNext()) {
            mDirector = cursor.getString(3);
            directorTitle = cursor.getString(1);
            mDirector = mDirector + "   =>   " + directorTitle;
            directorArrayList.add(mDirector);
        }

        cursor = database.rawQuery(movieCast, null);
        String mCast;
        String castTitle;
        while (cursor.moveToNext()) {
            mCast = cursor.getString(4);
            castTitle = cursor.getString(1);
            mCast = mCast + "   =>   " + castTitle;
            castArrayList.add(mCast);
        }

        combineArrayList.add(titleArrayList);
        combineArrayList.add(directorArrayList);
        combineArrayList.add(castArrayList);

        return combineArrayList;
    }

}