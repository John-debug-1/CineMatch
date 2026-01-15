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
public class TrendingController {

    @Autowired
    private TmdbService tmdbService;

    @GetMapping("/trending")
    public String trendingPage(Model model, HttpSession session) {

        List<Map<String, Object>> trendingMovies = tmdbService.getTrendingMovies(1);
        List<Map<String, Object>> trendingSeries = tmdbService.getTrendingSeries(1);

        model.addAttribute("movies", trendingMovies);
        model.addAttribute("series", trendingSeries);

        // Logged user
        Object loggedUser = session.getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);

        return "trending";
    }
}
