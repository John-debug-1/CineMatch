package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SentimentController {

    @GetMapping("/sentiment")
    public String sentimentPage(Model model, HttpSession session) {
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
        return "sentiment"; // -> sentiment.html
    }
}
