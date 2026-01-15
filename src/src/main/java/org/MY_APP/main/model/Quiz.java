package org.MY_APP.main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ποιος χρήστης έπαιξε το quiz
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int totalQuestions;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizQuestion> questions;

    // getters & setters

    public Long getId() { return id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public int getTotalQuestions() { return totalQuestions; }

    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<QuizQuestion> getQuestions() { return questions; }

    public void setQuestions(List<QuizQuestion> questions) { this.questions = questions; }
}
