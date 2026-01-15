package org.MY_APP.main.controller;

import org.MY_APP.main.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SortController {

    @Autowired
    private TmdbService tmdbService;

    @GetMapping("/sort")
    public String sortMovies(@RequestParam String type,
                             @RequestParam(defaultValue = "1") int page,
                             Model model) {

        if (type.equals("rating")) {
            model.addAttribute("movies", tmdbService.getMoviesByRating(page));
        } else if (type.equals("release")) {
            model.addAttribute("movies", tmdbService.getMoviesByReleaseDate(page));
        } else {
            model.addAttribute("movies", tmdbService.getMoviesByPopularity(page));
        }

        model.addAttribute("sortType", type);
        model.addAttribute("page", page);
        return "home"; // Ξαναχρησιμοποιούμε το ίδιο layout ✅
    }
}
