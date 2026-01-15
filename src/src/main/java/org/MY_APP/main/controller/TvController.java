package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class TvController {

    @Autowired
    private TmdbService tmdbService;

    @GetMapping("/tv/{id}")
    public String tvDetails(@PathVariable int id, Model model, HttpSession session) {

        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));

        Map<String, Object> tv = tmdbService.getTvDetails(id);

        // Trailer (YouTube)
        String trailerKey = null;
        Map<String, Object> videos = (Map<String, Object>) tv.get("videos");

        if (videos != null && videos.get("results") != null) {
            for (Map<String, Object> v : (Iterable<Map<String, Object>>) videos.get("results")) {
                if ("Trailer".equals(v.get("type"))) {
                    trailerKey = (String) v.get("key");
                    break;
                }
            }
        }

        model.addAttribute("tv", tv);
        model.addAttribute("trailerKey", trailerKey);

        return "tv-details";
    }

}
