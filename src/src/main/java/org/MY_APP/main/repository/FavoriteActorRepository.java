package org.MY_APP.main.repository;

import org.MY_APP.main.model.FavoriteActor;
import org.MY_APP.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public interface FavoriteActorRepository extends JpaRepository<FavoriteActor, Long> {

    boolean existsByUserAndActorId(User user, int actorId);

    Optional<FavoriteActor> findByUserAndActorId(User user, int actorId);

    List<FavoriteActor> findByUser(User user);

}
