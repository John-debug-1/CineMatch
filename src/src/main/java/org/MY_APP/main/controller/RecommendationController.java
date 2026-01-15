package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.model.Favorite;
import org.MY_APP.main.model.User;
import org.MY_APP.main.repository.FavoriteRepository;
import org.MY_APP.main.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class RecommendationController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private TmdbService tmdbService;

    @GetMapping("/recommendations")
    public List<Map<String, Object>> getRecommendations(
            HttpSession session,
            @RequestParam(defaultValue = "1") int page
    ) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return List.of(); // ❌ ΟΧΙ logged in → τίποτα

        List<Favorite> favorites = favoriteRepository.findByUser(user);
        if (favorites.isEmpty()) return List.of(); // ❌ Δεν έχει likes → τίποτα

        // Παίρνουμε ΕΝΑ τυχαίο liked movie để φέρουμε similar
        Favorite seed = favorites.get(new Random().nextInt(favorites.size()));

        return tmdbService.getSimilarMovies(seed.getMovieId(), page);
    }
}
