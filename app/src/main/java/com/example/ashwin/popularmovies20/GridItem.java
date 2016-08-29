package com.example.ashwin.popularmovies20;

/**
 * Created by ashwin on 8/21/16.
 */
public class GridItem {

    private String image;
    private String title;
    private String averageRating;
    private String synopsis;
    private String releaseDate;


    public GridItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
