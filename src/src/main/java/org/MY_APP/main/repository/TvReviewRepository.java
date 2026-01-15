package org.MY_APP.main.repository;

import org.MY_APP.main.model.TvReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TvReviewRepository extends JpaRepository<TvReview, Long> {

    Optional<TvReview> findByUserIdAndTvId(Long userId, Integer tvId);

    List<TvReview> findByTvIdOrderByCreatedAtDesc(Integer tvId);

    long countByTvId(Integer tvId);

    @Query("select avg(r.rating) from TvReview r where r.tvId = :tvId")
    Double avgRating(Integer tvId);
}
