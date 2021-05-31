package com.mad.w1761094;

public class MovieModel {
    //Declaring the variables
    private int id;
    private String movieTitle;
    private int movieYear;
    private String movieDirector;
    private String movieCast;
    private int movieRatings;
    private String movieReview;
    private int movieFavourite;

    //Args constructor
    public MovieModel(int id, String movieTitle, int movieYear, String movieDirector, String movieCast, int movieRatings, String movieReview, int movieFavourite) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.movieYear = movieYear;
        this.movieDirector = movieDirector;
        this.movieCast = movieCast;
        this.movieRatings = movieRatings;
        this.movieReview = movieReview;
        this.movieFavourite = movieFavourite;
    }

    //Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public int getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(int movieYear) {
        this.movieYear = movieYear;
    }

    public String getMovieDirector() {
        return movieDirector;
    }

    public void setMovieDirector(String movieDirector) {
        this.movieDirector = movieDirector;
    }

    public String getMovieCast() {
        return movieCast;
    }

    public void setMovieCast(String movieCast) {
        this.movieCast = movieCast;
    }

    public int getMovieRatings() {
        return movieRatings;
    }

    public void setMovieRatings(int movieRatings) {
        this.movieRatings = movieRatings;
    }

    public String getMovieReview() {
        return movieReview;
    }

    public void setMovieReview(String movieReview) {
        this.movieReview = movieReview;
    }

    public int getMovieFavourite() {
        return movieFavourite;
    }

    public void setMovieFavourite(int movieFavourite) {
        this.movieFavourite = movieFavourite;
    }
}
