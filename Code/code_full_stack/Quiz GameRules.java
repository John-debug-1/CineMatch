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
            if (text == null || text.isBlank()) throw new IllegalArgumentException("Question text cannot be empty.");
            if (type == null) throw new IllegalArgumentException("Question type cannot be null.");
            if (correct == null || correct.isBlank()) throw new IllegalArgumentException("Correct answer cannot be empty.");

            this.text = text;
            this.type = type;
            this.choices = (choices == null) ? List.of() : List.copyOf(choices);
            this.correct = normalize(correct);
        }

        public String getText() { return text; }
        public QuestionType getType() { return type; }
        public List<String> getChoices() { return choices; }
        public String getCorrect() { return correct; }

        public boolean isCorrect(String answer)
        {
            if (answer == null) return false;
            return correct.equals(normalize(answer));
        }

        private static String normalize(String s)
        {
            return s.trim().toLowerCase(Locale.ROOT);
        }
    }

    private int rounds = 5;
    private int secondsPerQuestion = 15;

    private final Map<QuestionType, Integer> points = new EnumMap<>(QuestionType.class);
    private final List<Question> questions = new ArrayList<>();
    private final Random random;

    // Για να μην επαναλαμβάνονται, κρατάμε “deck”
    private final Deque<Question> questionDeck = new ArrayDeque<>();
    private boolean noRepeats = false;

    public GameRules()
    {
        this(new Random());
    }

    public GameRules(Random random)
    {
        this.random = Objects.requireNonNull(random, "random cannot be null");

        points.put(QuestionType.MULTIPLE, 10);
        points.put(QuestionType.TRUE_FALSE, 5);
        points.put(QuestionType.OPEN, 15);
    }

    // -------------------------
    // Settings / Configuration
    // -------------------------

    public void setRounds(int r)
    {
        if (r <= 0) throw new IllegalArgumentException("Rounds must be > 0");
        rounds = r;
    }

    public void setSeconds(int sec)
    {
        if (sec <= 0) throw new IllegalArgumentException("Seconds per question must be > 0");
        secondsPerQuestion = sec;
    }

    public void setNoRepeats(boolean enabled)
    {
        noRepeats = enabled;
        rebuildDeck();
    }

    public boolean isNoRepeats()
    {
        return noRepeats;
    }

    public void setPoints(QuestionType type, int pts)
    {
        if (type == null) throw new IllegalArgumentException("Type cannot be null");
        if (pts < 0) throw new IllegalArgumentException("Points cannot be negative");
        points.put(type, pts);
    }

    public int getPointsForType(QuestionType type)
    {
        return points.getOrDefault(type, 0);
    }

    // -------------------------
    // Question Management
    // -------------------------

    public void addQuestion(Question q)
    {
        if (q == null) throw new IllegalArgumentException("Question cannot be null");
        questions.add(q);
        if (noRepeats) rebuildDeck();
    }

    public boolean removeQuestion(Question q)
    {
        boolean removed = questions.remove(q);
        if (removed && noRepeats) rebuildDeck();
        return removed;
    }

    public void clearQuestions()
    {
        questions.clear();
        questionDeck.clear();
    }

    public int getQuestionsCount()
    {
        return questions.size();
    }

    // -------------------------
    // Getting Questions
    // -------------------------

    public Question getRandomQuestion()
    {
        if (questions.isEmpty()) return null;

        if (!noRepeats)
        {
            // classic random (can repeat)
            return questions.get(random.nextInt(questions.size()));
        }

        // no repeat mode: pull from deck, reshuffle when empty
        if (questionDeck.isEmpty()) rebuildDeck();
        return questionDeck.pollFirst();
    }

    private void rebuildDeck()
    {
        questionDeck.clear();
        if (questions.isEmpty()) return;

        List<Question> copy = new ArrayList<>(questions);
        Collections.shuffle(copy, random);
        questionDeck.addAll(copy);
    }

    // -------------------------
    // Scoring / Evaluation
    // -------------------------

    public int evaluate(Question q, String answer, int timeUsed)
    {
        if (q == null) return 0;
        if (answer == null) return 0;

        // clamp timeUsed to [0..secondsPerQuestion]
        timeUsed = Math.max(0, Math.min(timeUsed, secondsPerQuestion));

        boolean correct = switch (q.getType())
        {
            case TRUE_FALSE -> isTrueFalseCorrect(q, answer);
            default -> q.isCorrect(answer);
        };

        if (!correct) return 0;

        int base = points.getOrDefault(q.getType(), 0);
        int bonus = Math.max(0, secondsPerQuestion - timeUsed);
        return base + bonus;
    }

    private boolean isTrueFalseCorrect(Question q, String answer)
    {
        String a = normalize(answer);
        String c = q.getCorrect();

        // Accept more variants
        a = mapTrueFalseAliases(a);
        c = mapTrueFalseAliases(c);

        return c.equals(a);
    }

    private String mapTrueFalseAliases(String s)
    {
        // map common Greek/English variants
        return switch (s)
        {
            case "t", "true", "yes", "y", "ναι" -> "true";
            case "f", "false", "no", "n", "οχι", "όχι" -> "false";
            default -> s;
        };
    }

    private static String normalize(String s)
    {
        return s.trim().toLowerCase(Locale.ROOT);
    }

    // -------------------------
    // Getters
    // -------------------------

    public int getRounds() { return rounds; }
    public int getSecondsPerQuestion() { return secondsPerQuestion; }
}
