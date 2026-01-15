package org.MY_APP.main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String query;

    @Column(name = "searched_at")
    private LocalDateTime searchedAt = LocalDateTime.now();

    // ✅ Constructors
    public SearchHistory() {}

    public SearchHistory(User user, String query) {
        this.user = user;
        this.query = query;
        this.searchedAt = LocalDateTime.now();
    }

    // ✅ Getters & Setters
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public LocalDateTime getSearchedAt() {
        return searchedAt;
    }

    public void setSearchedAt(LocalDateTime searchedAt) {
        this.searchedAt = searchedAt;
    }
}
