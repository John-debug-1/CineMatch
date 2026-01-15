package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.model.MovieReview;
import org.MY_APP.main.model.User;
import org.MY_APP.main.service.MovieReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class MovieReviewRestController {

    private final MovieReviewService service;

    public MovieReviewRestController(MovieReviewService service) {
        this.service = service;
    }

    private User requireUser(HttpSession session) {
        Object u = session.getAttribute("loggedUser");
        if (u == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        return (User) u;
    }

    // GET list + stats
    @GetMapping("/movie/{movieId}")
    public Map<String, Object> getMovieReviews(@PathVariable Integer movieId) {
        List<MovieReview> list = service.getAll(movieId);
        double avg = service.getAvg(movieId);
        long count = service.getCount(movieId);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        var mapped = list.stream().map(r -> Map.of(
                "id", r.getId(),
                "userId", r.getUser().getId(),
                "fullName", r.getUser().getFullName(),
                "rating", r.getRating(),
                "text", r.getReviewText(),
                "createdAt", r.getCreatedAt() == null ? null : r.getCreatedAt().format(fmt)
        )).toList();

        return Map.of(
                "avg", avg,
                "count", count,
                "reviews", mapped
        );
    }

    // GET my review
    @GetMapping("/movie/{movieId}/mine")
    public Map<String, Object> getMine(@PathVariable Integer movieId, HttpSession session) {
        User user = requireUser(session);
        MovieReview r = service.getMine(user, movieId);
        if (r == null) return Map.of("exists", false);

        return Map.of(
                "exists", true,
                "rating", r.getRating(),
                "text", r.getReviewText()
        );
    }

    // POST upsert
    @PostMapping("/movie/{movieId}")
    public Map<String, Object> submit(@PathVariable Integer movieId,
                                      @RequestBody Map<String, Object> body,
                                      HttpSession session) {
        User user = requireUser(session);

        Integer rating = body.get("rating") == null ? null : ((Number) body.get("rating")).intValue();
        String text = body.get("text") == null ? null : body.get("text").toString();

        MovieReview saved = service.upsertReview(user, movieId, rating, text);

        return Map.of(
                "ok", true,
                "id", saved.getId()
        );
    }
}
