package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.dto.MovieDetailsDto; // Import το DTO που φτιάξαμε
import org.MY_APP.main.repository.FavoriteRepository; // Import το Repository
import org.MY_APP.main.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class MovieController {

    @Autowired
    private TmdbService tmdbService;

    @Autowired
    private FavoriteRepository favoriteRepository; // 1. Προσθήκη του Repository

    @GetMapping("/movie/{id}")
    public String movieDetails(@PathVariable int id, Model model, HttpSession session) {

        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));

        // Παίρνουμε τα raw δεδομένα από το API (Map)
        Map<String, Object> rawMovieData = tmdbService.getMovieDetails(id);

        // 2. Δημιουργία και γέμισμα του DTO
        MovieDetailsDto movieDto = new MovieDetailsDto();

        // --- Mapping από το Map στο DTO ---
        movieDto.setId(String.valueOf(rawMovieData.get("id")));
        movieDto.setTitle((String) rawMovieData.get("title"));
        movieDto.setOverview((String) rawMovieData.get("overview"));

        // Χειρισμός εικόνας (αν υπάρχει)
        if (rawMovieData.get("poster_path") != null) {
            movieDto.setPosterPath("https://image.tmdb.org/t/p/w500" + rawMovieData.get("poster_path"));
        } else {
            // Βάλε μια default εικόνα ή null
            movieDto.setPosterPath("/images/no-image.png");
        }

        movieDto.setReleaseDate((String) rawMovieData.get("release_date"));

        // --- ΓΕΜΙΣΜΑ KPIs ---

        // KPI 1: Rating (Προσοχή στη μετατροπή αριθμών)
        movieDto.setTmdbRating(objToDouble(rawMovieData.get("vote_average")));

        // KPI 2: Popularity
        movieDto.setPopularity(objToDouble(rawMovieData.get("popularity")));

        // KPI 3: Local Likes (CineMatch Likes)
        // Μετράμε πόσες φορές υπάρχει το ID στον πίνακα favorites
        int localLikes = favoriteRepository.countByMovieId(id);
        movieDto.setCineMatchLikes(localLikes);


        // --- Λογική για το Trailer (παραμένει ως έχει) ---
        String trailerKey = null;
        Map<String, Object> videos = (Map<String, Object>) rawMovieData.get("videos");
        if (videos != null && videos.get("results") != null) {
            for (Map<String, Object> v : (Iterable<Map<String, Object>>) videos.get("results")) {
                if ("Trailer".equals(v.get("type"))) {
                    trailerKey = (String) v.get("key");
                    break;
                }
            }
        }

        // 3. Στέλνουμε το DTO στο View αντί για το Map
        model.addAttribute("movie", movieDto);
        model.addAttribute("trailerKey", trailerKey);

        return "movie-details";
    }

    // Βοηθητική μέθοδος για ασφαλή μετατροπή σε Double
    // (To API μπορεί να στείλει Integer π.χ. 8 αντί για 8.0, και το casting σκάει)
    private Double objToDouble(Object obj) {
        if (obj == null) return 0.0;
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        try {
            return Double.parseDouble(obj.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}