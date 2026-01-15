package org.MY_APP.main;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.model.Favorite;
import org.MY_APP.main.model.User;
import org.MY_APP.main.repository.FavoriteRepository;
import org.MY_APP.main.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private TmdbService tmdbService;

    @Autowired
    private FavoriteRepository favoriteRepository;

    private List<Map<String, Object>> take(List<Map<String, Object>> list, int n) {
        if (list == null) return Collections.emptyList();
        return list.size() <= n ? list : list.subList(0, n);
    }

    @GetMapping({"/", "/home"})
    public String home(Model model, HttpSession session) {

        // 🔥 ΠΕΡΝΑΜΕ loggedUser στο template
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));

        model.addAttribute("results", null);
        model.addAttribute("movies", take(tmdbService.getTrendingMovies(1), 20));
        model.addAttribute("tvshows", take(tmdbService.getTrendingSeries(1), 20));
        model.addAttribute("streaming", take(tmdbService.getStreamingPopular(1), 20));
        model.addAttribute("topRated", take(tmdbService.getTopRatedMovies(1), 20));
        model.addAttribute("latestMovies", take(tmdbService.getLatestMovies(1), 20));
        model.addAttribute("actors", take(tmdbService.getPopularActors(), 20));

        User user = (User) session.getAttribute("loggedUser");

        if (user != null) {
            List<Favorite> favorites = favoriteRepository.findByUser(user);

            if (!favorites.isEmpty()) {
                Favorite seed = favorites.get(0);
                model.addAttribute(
                        "recommendedMovies",
                        take(tmdbService.getSimilarMovies(seed.getMovieId(), 1), 20)
                );
            }
        }

        model.addAttribute(
                "topDirectors",
                tmdbService.getTopRatedDirectors(1)
        );

        return "home";
    }
}
