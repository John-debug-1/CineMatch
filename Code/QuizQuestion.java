package org.MY_APP.main.model;

import jakarta.persistence.*;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"quiz", "answers", "hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "quiz_question")
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false, columnDefinition = "TEXT", length = 500)
    private String questionText;

    @Column(nullable = false, columnDefinition = "TEXT", length = 255)
    private String optionA;

    @Column(nullable = false, columnDefinition = "TEXT", length = 255)
    private String optionB;

    @Column(nullable = false, columnDefinition = "TEXT", length = 255)
    private String optionC;

    @Column(nullable = false, columnDefinition = "TEXT", length = 255)
    private String optionD;

    // 'A', 'B', 'C' ή 'D'
    @Column(nullable = false, length = 255)
    private String correctOption;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizAnswer> answers;

    // getters & setters

    public Long getId() { return id; }

    public Quiz getQuiz() { return quiz; }

    public void setQuiz(Quiz quiz) { this.quiz = quiz; }

    public String getQuestionText() { return questionText; }

    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getOptionA() { return optionA; }

    public void setOptionA(String optionA) { this.optionA = optionA; }

    public String getOptionB() { return optionB; }

    public void setOptionB(String optionB) { this.optionB = optionB; }

    public String getOptionC() { return optionC; }

    public void setOptionC(String optionC) { this.optionC = optionC; }

    public String getOptionD() { return optionD; }

    public void setOptionD(String optionD) { this.optionD = optionD; }

    public String getCorrectOption() { return correctOption; }

    public void setCorrectOption(String correctOption) { this.correctOption = correctOption; }

    public List<QuizAnswer> getAnswers() { return answers; }

    public void setAnswers(List<QuizAnswer> answers) { this.answers = answers; }
}
frnfkeebvajf
