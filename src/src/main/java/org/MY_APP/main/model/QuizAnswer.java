package org.MY_APP.main.model;

import jakarta.persistence.*;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"question", "hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "quiz_answer")
public class QuizAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion question;

    // A, B, C ή D
    @Column(nullable = false, length = 1)
    private String selectedOption;

    @Column(nullable = false)
    private boolean correct;

    // getters & setters

    public Long getId() { return id; }

    public QuizQuestion getQuestion() { return question; }

    public void setQuestion(QuizQuestion question) { this.question = question; }

    public String getSelectedOption() { return selectedOption; }

    public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }

    public boolean isCorrect() { return correct; }

    public void setCorrect(boolean correct) { this.correct = correct; }
}
