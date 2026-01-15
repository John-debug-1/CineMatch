package org.MY_APP.main.service;

import org.MY_APP.main.dto.AiQuizQuestionDto;
import org.MY_APP.main.model.*;
import org.MY_APP.main.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizAnswerRepository quizAnswerRepository;
    private final UserRepository userRepository;
    private final QuizAiService quizAiService;
    private final FavoriteRepository favoriteRepository;
    private final FavoriteActorRepository favoriteActorRepository;
    private final TmdbService tmdbService;

    public QuizService(QuizRepository quizRepository,
                       QuizQuestionRepository quizQuestionRepository,
                       QuizAnswerRepository quizAnswerRepository,
                       UserRepository userRepository,
                       QuizAiService quizAiService,
                       FavoriteRepository favoriteRepository,
                       FavoriteActorRepository favoriteActorRepository,
                       TmdbService tmdbService) {
        this.quizRepository = quizRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizAnswerRepository = quizAnswerRepository;
        this.userRepository = userRepository;
        this.quizAiService = quizAiService;
        this.favoriteRepository = favoriteRepository;
        this.favoriteActorRepository = favoriteActorRepository;
        this.tmdbService = tmdbService;
    }

    @Transactional
    public Quiz startQuiz(User user) {
        List<Favorite> favorites = favoriteRepository.findByUser(user);

        if (favorites.size() < 2) {
            return buildAndSaveQuiz(user, quizAiService.generateQuizFromAI());
        }

        List<AiQuizQuestionDto> questions = new ArrayList<>();
        Collections.shuffle(favorites);
        Random random = new Random();

        // Επιλέγουμε μέχρι 8 ταινίες για ποικιλία
        List<Favorite> subList = favorites.subList(0, Math.min(8, favorites.size()));

        for (Favorite fav : subList) {
            try {
                AiQuizQuestionDto dto = new AiQuizQuestionDto();
                int type = random.nextInt(3); // 0: Ηθοποιός, 1: Έτος, 2: Τίτλος

                if (type == 0) {
                    List<Map<String, Object>> cast = tmdbService.getMovieCast(fav.getMovieId());
                    if (cast != null && !cast.isEmpty()) {
                        String realActor = (String) cast.get(0).get("name");
                        dto.setQuestion("Ποιος ηθοποιός πρωταγωνιστεί στην αγαπημένη σου ταινία '" + fav.getTitle() + "';");
                        setupOptions(dto, realActor, "Brad Pitt", "Leonardo DiCaprio", "Tom Cruise");
                    }
                }
                else if (type == 1) {
                    Map<String, Object> details = tmdbService.getMovieDetails(fav.getMovieId());
                    String releaseDate = (String) details.get("release_date");
                    String year = (releaseDate != null) ? releaseDate.split("-")[0] : "2010";

                    dto.setQuestion("Σε ποιο έτος κυκλοφόρησε η ταινία '" + fav.getTitle() + "' που έχεις στα αγαπημένα;");
                    setupOptions(dto, year, "1998", "2004", "2015");
                }
                else {
                    dto.setQuestion("Ποια από αυτές τις ταινίες είναι στη λίστα των αγαπημένων σου;");
                    setupOptions(dto, fav.getTitle(), "The Godfather", "The Matrix", "Inception");
                }

                if (dto.getQuestion() != null) questions.add(dto);
            } catch (Exception e) { continue; }
        }

        return buildAndSaveQuiz(user, questions.isEmpty() ? quizAiService.generateQuizFromAI() : questions);
    }

    @Transactional
    public Quiz startActorQuiz(User user) {
        List<FavoriteActor> actors = favoriteActorRepository.findByUser(user);
        if (actors.size() < 2) return buildAndSaveQuiz(user, quizAiService.generateActorQuizFromAI());

        List<AiQuizQuestionDto> questions = new ArrayList<>();
        Collections.shuffle(actors);

        for (FavoriteActor actor : actors) {
            AiQuizQuestionDto dto = new AiQuizQuestionDto();
            dto.setQuestion("Ποιος από τους παρακάτω είναι ένας από τους αγαπημένους σου ηθοποιούς;");
            setupOptions(dto, actor.getName(), "Morgan Freeman", "Al Pacino", "Robert De Niro");
            questions.add(dto);
            if (questions.size() >= 5) break;
        }
        return buildAndSaveQuiz(user, questions);
    }

    @Transactional
    public boolean submitAnswer(Long questionId, String selectedOption) {
        QuizQuestion question = quizQuestionRepository.findById(questionId).orElseThrow(() -> new RuntimeException("Question not found"));
        boolean isCorrect = question.getCorrectOption().equalsIgnoreCase(selectedOption);

        QuizAnswer answer = new QuizAnswer();
        answer.setQuestion(question);
        answer.setSelectedOption(selectedOption);
        answer.setCorrect(isCorrect);
        quizAnswerRepository.save(answer);

        if (isCorrect) {
            Quiz quiz = question.getQuiz();
            quiz.setScore(quiz.getScore() + 1);
            quizRepository.save(quiz);
        }
        return isCorrect;
    }

    @Transactional
    public Quiz finishQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        User user = quiz.getUser();
        user.addToTotalQuizScore(quiz.getScore());
        userRepository.save(user);
        return quiz;
    }

    private void setupOptions(AiQuizQuestionDto dto, String correct, String w1, String w2, String w3) {
        List<String> options = new ArrayList<>(List.of(correct, w1, w2, w3));
        Collections.shuffle(options);
        dto.setOptionA(options.get(0)); dto.setOptionB(options.get(1));
        dto.setOptionC(options.get(2)); dto.setOptionD(options.get(3));
        dto.setCorrect(options.get(0).equals(correct) ? "A" : options.get(1).equals(correct) ? "B" : options.get(2).equals(correct) ? "C" : "D");
    }

    private Quiz buildAndSaveQuiz(User user, List<AiQuizQuestionDto> aiQuestions) {
        Quiz quiz = new Quiz();
        quiz.setUser(user);
        quiz.setTotalQuestions(aiQuestions.size());
        quiz.setScore(0);
        quiz = quizRepository.save(quiz);
        List<QuizQuestion> questions = new ArrayList<>();
        for (AiQuizQuestionDto ai : aiQuestions) {
            QuizQuestion q = new QuizQuestion();
            q.setQuiz(quiz); q.setQuestionText(ai.getQuestion());
            q.setOptionA(ai.getOptionA()); q.setOptionB(ai.getOptionB());
            q.setOptionC(ai.getOptionC()); q.setOptionD(ai.getOptionD());
            q.setCorrectOption(ai.getCorrect());
            questions.add(q);
        }
        quizQuestionRepository.saveAll(questions);
        quiz.setQuestions(questions);
        return quiz;
    }

    public List<Quiz> getUserQuizzes(User user) { return quizRepository.findByUserOrderByCreatedAtDesc(user); }
}