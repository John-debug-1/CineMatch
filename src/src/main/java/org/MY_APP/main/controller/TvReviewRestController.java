package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.model.TvReview;
import org.MY_APP.main.model.User;
import org.MY_APP.main.service.TvReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tv-reviews")
public class TvReviewRestController {

    private final TvReviewService service;

    public TvReviewRestController(TvReviewService service) {
        this.service = service;
    }

    private User requireUser(HttpSession session) {
        Object u = session.getAttribute("loggedUser");
        if (u == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        return (User) u;
    }

    @GetMapping("/tv/{tvId}")
    public Map<String, Object> getTvReviews(@PathVariable Integer tvId) {
        List<TvReview> list = service.getAll(tvId);
        double avg = service.getAvg(tvId);
        long count = service.getCount(tvId);

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

    @GetMapping("/tv/{tvId}/mine")
    public Map<String, Object> getMine(@PathVariable Integer tvId, HttpSession session) {
        User user = requireUser(session);
        TvReview r = service.getMine(user, tvId);
        if (r == null) return Map.of("exists", false);

        return Map.of(
                "exists", true,
                "rating", r.getRating(),
                "text", r.getReviewText()
        );
    }

    @PostMapping("/tv/{tvId}")
    public Map<String, Object> submit(@PathVariable Integer tvId,
                                      @RequestBody Map<String, Object> body,
                                      HttpSession session) {
        User user = requireUser(session);

        Integer rating = body.get("rating") == null ? null : ((Number) body.get("rating")).intValue();
        String text = body.get("text") == null ? null : body.get("text").toString();

        TvReview saved = service.upsertReview(user, tvId, rating, text);

        return Map.of("ok", true, "id", saved.getId());
    }
}
