package org.MY_APP.main.repository;

import org.MY_APP.main.model.FavoriteTv;
import org.MY_APP.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteTvRepository extends JpaRepository<FavoriteTv, Long> {

    boolean existsByUserAndTvId(User user, int tvId);

    Optional<FavoriteTv> findByUserAndTvId(User user, int tvId);
}
