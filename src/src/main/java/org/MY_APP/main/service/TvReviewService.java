package org.MY_APP.main.service;

import org.MY_APP.main.model.TvReview;
import org.MY_APP.main.model.User;
import org.MY_APP.main.repository.TvReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TvReviewService {

    private final TvReviewRepository repo;

    public TvReviewService(TvReviewRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public TvReview upsertReview(User user, Integer tvId, Integer rating, String text) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be 1..5");
        }

        TvReview r = repo.findByUserIdAndTvId(user.getId(), tvId).orElseGet(TvReview::new);

        r.setUser(user);
        r.setTvId(tvId);
        r.setRating(rating);
        r.setReviewText(text == null ? null : text.trim());

        return repo.save(r);
    }

    public TvReview getMine(User user, Integer tvId) {
        return repo.findByUserIdAndTvId(user.getId(), tvId).orElse(null);
    }

    public List<TvReview> getAll(Integer tvId) {
        return repo.findByTvIdOrderByCreatedAtDesc(tvId);
    }

    public double getAvg(Integer tvId) {
        Double avg = repo.avgRating(tvId);
        return avg == null ? 0.0 : avg;
    }

    public long getCount(Integer tvId) {
        return repo.countByTvId(tvId);
    }
}
