package org.MY_APP.main.controller;

import org.MY_APP.main.model.User;
import org.MY_APP.main.service.FavoriteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/favorites/add")
    public String addFavorite(@RequestParam int movieId, @RequestParam String title, @RequestParam String posterPath, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user != null) {
            favoriteService.addFavorite(user, movieId, title, posterPath);
        }
        return "redirect:/movie-details?id=" + movieId;
    }

    @PostMapping("/favorites/remove")
    public String removeFavorite(@RequestParam int movieId, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user != null) {
            favoriteService.removeFavorite(user, movieId);
        }
        return "redirect:/movie-details?id=" + movieId;
    }
}