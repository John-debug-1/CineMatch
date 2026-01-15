package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.model.User;
import org.MY_APP.main.repository.*;
import org.MY_APP.main.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private FavoriteActorRepository favoriteActorRepository;

    @Autowired
    private FavoriteDirectorRepository favoriteDirectorRepository;

    @Autowired
    private SearchHistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteService favoriteService;

    // ✅ PROFILE PAGE
    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("loggedUser", user);
        model.addAttribute("favorites", favoriteRepository.findByUser(user));
        model.addAttribute("history", historyRepository.findByUserOrderBySearchedAtDesc(user));

        // ✅ ACTORS
        model.addAttribute("favoriteActors", favoriteActorRepository.findByUser(user));

        // ✅ DIRECTORS
        model.addAttribute("favoriteDirectors", favoriteDirectorRepository.findByUser(user));

        return "profile";
    }

    // ✅ CLEAR SEARCH HISTORY
    @PostMapping("/history/clear")
    public String clearHistory(HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        if (user != null) {
            historyRepository.clearByUser(user);
        }

        return "redirect:/profile";
    }

    // ✅ DELETE ACCOUNT
    @PostMapping("/delete-account")
    public String deleteAccount(HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        if (user != null) {
            userRepository.delete(user);
            session.invalidate();
        }

        return "redirect:/";
    }
}
