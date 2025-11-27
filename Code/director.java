package org.MY_APP.Model;

import java.util.List;

// Η κλάση Director περιέχει τα στοιχεία ενός σκηνοθέτη, όπως προφίλ, βιογραφία και
// τις ταινίες που έχει σκηνοθετήσει, τα οποία λαμβάνονται από το TMDB API.

public class Director {

    private int id;                           // Μοναδικό TMDB ID σκηνοθέτη
    private String name;                      // Ονοματεπώνυμο
    private String profilePath;               // Σχετικό URL εικόνας προφίλ
    private String biography;                 // Βιογραφία σκηνοθέτη
    private String birthday;                  // Ημερομηνία γέννησης
    private double popularity;                // Δημοτικότητα στο TMDB
    private List<String> knownForTitles;      // Ταινίες για τις οποίες είναι γνωστός

    public Director() {}  // Απαραίτητο για JSON parsing

    // -------- Getters & Setters --------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public List<String> getKnownForTitles() {
        return knownForTitles;
    }

    public void setKnownForTitles(List<String> knownForTitles) {
        this.knownForTitles = knownForTitles;
    }
}
