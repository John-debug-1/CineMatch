package org.MY_APP.main.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite_tv",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "tv_id"})})
public class FavoriteTv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "tv_id", nullable = false)
    private int tvId;

    private String name;
    private String posterPath;

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public int getTvId() { return tvId; }
    public void setTvId(int tvId) { this.tvId = tvId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }
}
