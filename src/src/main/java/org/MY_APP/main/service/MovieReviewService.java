package org.MY_APP.main.service;

import org.MY_APP.main.model.MovieReview;
import org.MY_APP.main.model.User;
import org.MY_APP.main.repository.MovieReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieReviewService {

    private final MovieReviewRepository repo;

    public MovieReviewService(MovieReviewRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public MovieReview upsertReview(User user, Integer movieId, Integer rating, String text) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be 1..5");
        }

        MovieReview r = repo.findByUserIdAndMovieId(user.getId(), movieId)
                .orElseGet(MovieReview::new);

        r.setUser(user);
        r.setMovieId(movieId);
        r.setRating(rating);
        r.setReviewText(text == null ? null : text.trim());

        return repo.save(r);
    }

    public MovieReview getMine(User user, Integer movieId) {
        return repo.findByUserIdAndMovieId(user.getId(), movieId).orElse(null);
    }

    public List<MovieReview> getAll(Integer movieId) {
        return repo.findByMovieIdOrderByCreatedAtDesc(movieId);
    }

    public double getAvg(Integer movieId) {
        Double avg = repo.avgRating(movieId);
        return avg == null ? 0.0 : avg;
    }

    public long getCount(Integer movieId) {
        return repo.countByMovieId(movieId);
    }
}
