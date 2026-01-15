package org.MY_APP.main.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorites",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "movie_id"})})
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "movie_id", nullable = false)
    private int movieId;

    private String title;
    private String posterPath;

    // ✅ GETTERS / SETTERS
    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }
}
