package org.MY_APP.Game;

import java.util.*;

public class GameRules 
{

    public enum QuestionType { MULTIPLE, TRUE_FALSE, OPEN }

    public static class Question 
    {
        private final String text;
        private final QuestionType type;
        private final List<String> choices;
        private final String correct;

        public Question(String text, QuestionType type, List<String> choices, String correct) 
        {
            this.text = text;
            this.type = type;
            this.choices = choices == null ? List.of() : choices;
            this.correct = correct.trim().toLowerCase();
        }

        public String getText() { return text; }
        public QuestionType getType() { return type; }
        public List<String> getChoices() { return choices; }
        public String getCorrect() { return correct; }
    }

    private int rounds = 5;
    private int secondsPerQuestion = 15;
    private final Map<QuestionType, Integer> points = new EnumMap<>(QuestionType.class);
    private final List<Question> questions = new ArrayList<>();
    private final Random random = new Random();

    public GameRules() 
    {
        points.put(QuestionType.MULTIPLE, 10);
        points.put(QuestionType.TRUE_FALSE, 5);
        points.put(QuestionType.OPEN, 15);
    }

    public void setRounds(int r) { rounds = r; }
    public void setSeconds(int sec) { secondsPerQuestion = sec; }
    public void addQuestion(Question q) { questions.add(q); }

    public Question getRandomQuestion() 
    {
        return questions.isEmpty() ? null : questions.get(random.nextInt(questions.size()));
    }

    public int evaluate(Question q, String answer, int timeUsed) 
    {
        if (!q.correct.equals(answer.trim().toLowerCase())) return 0;
        int base = points.get(q.type);
        int bonus = Math.max(0, secondsPerQuestion - timeUsed);
        return base + bonus ;
    }

    
    public int getRounds() { return rounds; }
    public int getSecondsPerQuestion() { return secondsPerQuestion; }
}




