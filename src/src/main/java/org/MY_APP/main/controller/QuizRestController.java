package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.model.Quiz;
import org.MY_APP.main.model.User;
import org.MY_APP.main.service.QuizService;
import org.MY_APP.main.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
public class QuizRestController {

    private final QuizService quizService;
    private final UserService userService;

    public QuizRestController(QuizService quizService, UserService userService) {
        this.quizService = quizService;
        this.userService = userService;
    }

    // ✅ QUIZ 1 — CLASSIC
    @PostMapping("/start")
    public ResponseEntity<?> startQuiz(HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }

        Quiz quiz = quizService.startQuiz(user);
        return ResponseEntity.ok(quiz);
    }


    // ✅ QUIZ 2 — ACTOR GUESS
    @PostMapping("/start-actor")
    public ResponseEntity<?> startActorQuiz(HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }

        Quiz quiz = quizService.startActorQuiz(user);
        return ResponseEntity.ok(quiz);
    }

    // ✅ SUBMIT ANSWER
    @PostMapping("/answer")
    public ResponseEntity<?> submitAnswer(@RequestBody Map<String, String> payload) {

        Long questionId = Long.parseLong(payload.get("questionId"));
        String selectedOption = payload.get("selectedOption");

        boolean correct = quizService.submitAnswer(questionId, selectedOption);

        return ResponseEntity.ok(Map.of("correct", correct));
    }

    // ✅ FINISH QUIZ
    @PostMapping("/finish/{quizId}")
    public ResponseEntity<Quiz> finishQuiz(@PathVariable Long quizId) {
        Quiz quiz = quizService.finishQuiz(quizId);
        return ResponseEntity.ok(quiz);
    }

    // ✅ HISTORY
    @GetMapping("/history")
    public ResponseEntity<List<Quiz>> history(
            @SessionAttribute("loggedUser") User user) {
        return ResponseEntity.ok(quizService.getUserQuizzes(user));
    }
}
