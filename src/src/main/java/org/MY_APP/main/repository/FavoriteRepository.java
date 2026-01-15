package org.MY_APP.main.repository;

import org.MY_APP.main.model.Favorite;
import org.MY_APP.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUser(User user);

    boolean existsByUserAndMovieId(User user, int movieId);

    Optional<Favorite> findByUserAndMovieId(User user, int movieId);

    // Μετράει πόσες εγγραφές υπάρχουν με αυτό το movieId
    int countByMovieId(int movieId);
}
