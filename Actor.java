package org.MY_APP.Model;

import java.util.List;

/**
 * Αντιπροσωπεύει έναν ηθοποιό όπως επιστρέφεται από το TMDB.
 * Περιέχει βασικά στοιχεία όπως όνομα, ημερομηνία γέννησης,
 * εικόνα προφίλ, βιογραφία και βαθμό δημοτικότητας.
 */
public class Actor {

    private int id;                            // Μοναδικό ID ηθοποιού από TMDB
    private String name;                       // Ονοματεπώνυμο ηθοποιού
    private String profilePath;                // Σχετικό URL εικόνας προφίλ
    private String biography;                  // Σύντομη βιογραφία
    private String birthday;                   // Ημερομηνία γέννησης
    private double popularity;                 // Βαθμός δημοτικότητας στο TMDB
    private List<String> knownForTitles;       // Τίτλοι όπου ο ηθοποιός είναι γνωστός

    public Actor() {}  // Κενός constructor για parsing JSON

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
