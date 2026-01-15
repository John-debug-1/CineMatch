package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.model.User;
import org.MY_APP.main.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FavoriteRestController {

    @Autowired
    private FavoriteService favoriteService;

    private User logged(HttpSession session) {
        return (User) session.getAttribute("loggedUser");
    }

    // ---------------- MOVIES ----------------
    @GetMapping("/favorites/is-favorite/{movieId}")
    public boolean isFavMovie(@PathVariable int movieId, HttpSession session) {
        User u = logged(session);
        return u != null && favoriteService.isFavoriteMovie(u, movieId);
    }

    @PostMapping("/favorites/add/{movieId}")
    public void addMovie(@PathVariable int movieId, HttpSession session) {
        User u = logged(session);
        if (u != null) favoriteService.addFavoriteMovieById(u, movieId);
    }

    @PostMapping("/favorites/remove/{movieId}")
    public void removeMovie(@PathVariable int movieId, HttpSession session) {
        User u = logged(session);
        if (u != null) favoriteService.removeFavorite(u, movieId);
    }

    // ---------------- TV ----------------
    @GetMapping("/favorites/tv/is-favorite/{tvId}")
    public boolean isFavTv(@PathVariable int tvId, HttpSession session) {
        User u = logged(session);
        return u != null && favoriteService.isFavoriteTv(u, tvId);
    }

    @PostMapping("/favorites/tv/add/{tvId}")
    public void addTv(@PathVariable int tvId, HttpSession session) {
        User u = logged(session);
        if (u != null) favoriteService.addFavoriteTvById(u, tvId);
    }

    @PostMapping("/favorites/tv/remove/{tvId}")
    public void removeTv(@PathVariable int tvId, HttpSession session) {
        User u = logged(session);
        if (u != null) favoriteService.removeFavoriteTv(u, tvId);
    }

    // ---------------- ACTORS ----------------
    @GetMapping("/favorites/actor/is-favorite/{actorId}")
    public boolean isFavActor(@PathVariable int actorId, HttpSession session) {
        User u = logged(session);
        return u != null && favoriteService.isFavoriteActor(u, actorId);
    }

    @PostMapping("/favorites/actor/add/{actorId}")
    public void addActor(@PathVariable int actorId, HttpSession session) {
        User u = logged(session);
        if (u != null) favoriteService.addFavoriteActorById(u, actorId);
    }

    @PostMapping("/favorites/actor/remove/{actorId}")
    public void removeActor(@PathVariable int actorId, HttpSession session) {
        User u = logged(session);
        if (u != null) favoriteService.removeFavoriteActor(u, actorId);
    }

    // ---------------- DIRECTORS ----------------
    @GetMapping("/favorites/director/is-favorite/{directorId}")
    public boolean isFavDirector(@PathVariable int directorId, HttpSession session) {
        User u = logged(session);
        return u != null && favoriteService.isFavoriteDirector(u, directorId);
    }

    @PostMapping("/favorites/director/add/{directorId}")
    public void addDirector(@PathVariable int directorId, HttpSession session) {
        User u = logged(session);
        if (u != null) favoriteService.addFavoriteDirectorById(u, directorId);
    }

    @PostMapping("/favorites/director/remove/{directorId}")
    public void removeDirector(@PathVariable int directorId, HttpSession session) {
        User u = logged(session);
        if (u != null) favoriteService.removeFavoriteDirector(u, directorId);
    }
}
