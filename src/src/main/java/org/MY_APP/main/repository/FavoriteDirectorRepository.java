package org.MY_APP.main.repository;

import org.MY_APP.main.model.FavoriteDirector;
import org.MY_APP.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteDirectorRepository extends JpaRepository<FavoriteDirector, Long> {

    boolean existsByUserAndDirectorId(User user, int directorId);

    Optional<FavoriteDirector> findByUserAndDirectorId(User user, int directorId);

    List<FavoriteDirector> findByUser(User user);
}
