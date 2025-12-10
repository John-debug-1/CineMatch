package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.MY_APP.main.model.SearchHistory;
import org.MY_APP.main.model.User;
import org.MY_APP.main.repository.SearchHistoryRepository;

import java.util.Map;

@Controller
public class SearchController {

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Autowired
    private TmdbService tmdbService;

    // Αν ο χρήστης πατήσει /search χωρίς query → πάει στην αρχική
    @GetMapping("/search")
    public String redirectToHome() {
        return "redirect:/home";
    }

    // Σωστό: όταν κάνουμε POST από τη φόρμα
    @PostMapping("/search")
    public String handleSearch(
            @RequestParam("query") String query,
            Model model,
            HttpSession session
    ) {
        Map<String, Object> results = tmdbService.search(query);

        model.addAttribute("query", query);
        model.addAttribute("results", results);

        // ✅ Logged User
        Object loggedUserObj = session.getAttribute("loggedUser");

        if (loggedUserObj != null) {
            User loggedUser = (User) loggedUserObj

            // ✅ Αποθήκευση στο DB
            SearchHistory history = new SearchHistory(loggedUser, query);
            searchHistoryRepository.save(history);

            model.addAttribute("loggedUser", loggedUser);
        }

        return "search";
    }
}
