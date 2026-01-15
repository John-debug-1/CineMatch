package org.MY_APP.main.dto;

import java.util.List;

public class MovieDetailsDto {

    // --- Βασικά Στοιχεία Ταινίας ---
    private String id;              // Το ID της ταινίας
    private String title;           // Ο τίτλος
    private String overview;        // Η περιγραφή (plot)
    private String posterPath;      // Το URL/Path της εικόνας
    private String releaseDate;     // Ημερομηνία κυκλοφορίας
    private List<String> genres;    // Λίστα με τα είδη (π.χ. Action, Drama) - Προαιρετικό

    // --- KPIs (Δείκτες) ---
    private Double tmdbRating;      // KPI 1: Βαθμολογία από TMDB (π.χ. 8.5)
    private Double popularity;      // KPI 2: Δημοτικότητα από TMDB
    private int cineMatchLikes;     // KPI 3: Likes από τη δική σου βάση (Local Favorites)

    // --- Constructors ---

    // Κενός Constructor (απαραίτητος)
    public MovieDetailsDto() {
    }

    // Constructor με όλα τα πεδία (για ευκολία)
    public MovieDetailsDto(String id, String title, String overview, String posterPath,
                           String releaseDate, Double tmdbRating, Double popularity, int cineMatchLikes) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.tmdbRating = tmdbRating;
        this.popularity = popularity;
        this.cineMatchLikes = cineMatchLikes;
    }

    // --- Getters και Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    // --- KPI Getters/Setters ---

    public Double getTmdbRating() {
        return tmdbRating;
    }

    public void setTmdbRating(Double tmdbRating) {
        this.tmdbRating = tmdbRating;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public int getCineMatchLikes() {
        return cineMatchLikes;
    }

    public void setCineMatchLikes(int cineMatchLikes) {
        this.cineMatchLikes = cineMatchLikes;
    }

    // --- ToString (Χρήσιμο για debugging / System.out.println) ---
    @Override
    public String toString() {
        return "MovieDetailsDto{" +
                "title='" + title + '\'' +
                ", tmdbRating=" + tmdbRating +
                ", cineMatchLikes=" + cineMatchLikes +
                '}';
    }
}