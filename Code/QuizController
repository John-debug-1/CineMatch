package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuizController {

    @GetMapping("/quiz-selection")
    public String quizSelection(HttpSession session, Model model) {

        // Add logged user
        Object loggedUser = session.getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);

        return "quiz-selection";
    }

    @GetMapping("/quiz1")
    public String quiz1(HttpSession session, Model model) {

        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
        return "quiz1";
    }

    @GetMapping("/quiz2")
    public String quiz2(HttpSession session, Model model) {

        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
        return "quiz2";
    }
}
