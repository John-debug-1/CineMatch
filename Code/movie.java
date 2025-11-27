package org.MY_APP.Model;

// Η κλάση Movie αναπαριστά μια ταινία και περιλαμβάνει τίτλο, ημερομηνία κυκλοφορίας,
// περιγραφή, αφίσα, δημοτικότητα και βαθμολογία από το TMDB API.

public class Movie {

    private int id;                  // Μοναδικό TMDB ID ταινίας
    private String title;            // Τίτλος ταινίας
    private String releaseDate;      // Ημερομηνία κυκλοφορίας
    private String overview;         // Περιγραφή / σύνοψη
    private String posterPath;       // Σχετικό path αφίσας
    private double popularity;       // Δημοτικότητα
    private double voteAverage;      // Μέση βαθμολογία χρηστών
    private String posterUrl;        // Πλήρες URL αφίσας

    public Movie() {}  // Constructor για JSON parsing

    public Movie(int id, String title, String releaseDate, String overview,
                 String posterPath, double popularity, double voteAverage) {

        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.posterPath = posterPath;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
    }

    // -------- Getters & Setters --------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
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

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
