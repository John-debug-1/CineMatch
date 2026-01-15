package org.MY_APP.main.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite_directors")   // ✅ ΚΡΙΣΙΜΟ FIX
public class FavoriteDirector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "director_id")
    private int directorId;

    private String name;

    @Column(name = "profile_path")
    private String profilePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // ✅ GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public int getDirectorId() {
        return directorId;
    }

    public void setDirectorId(int directorId) {
        this.directorId = directorId;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
