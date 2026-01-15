package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class TrendingCelebritiesController {

    @Autowired
    private TmdbService tmdbService;

    @GetMapping("/trending-celebrities")
    public String trendingCelebrities(Model model, HttpSession session) {

        // ✅ ΧΡΗΣΙΜΟΠΟΙΟΥΜΕ ΤΟ ΥΠΑΡΧΟΝ METHOD
        List<Map<String, Object>> celebrities = tmdbService.getTrendingActors();

        model.addAttribute("celebrities", celebrities);

        Object loggedUser = session.getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);

        return "trending-celebrities";
    }
}
