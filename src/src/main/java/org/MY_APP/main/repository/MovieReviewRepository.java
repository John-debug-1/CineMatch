package org.MY_APP.main.repository;

import org.MY_APP.main.model.MovieReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MovieReviewRepository extends JpaRepository<MovieReview, Long> {

    Optional<MovieReview> findByUserIdAndMovieId(Long userId, Integer movieId);

    List<MovieReview> findByMovieIdOrderByCreatedAtDesc(Integer movieId);

    long countByMovieId(Integer movieId);

    @Query("select avg(r.rating) from MovieReview r where r.movieId = :movieId")
    Double avgRating(Integer movieId);
}
