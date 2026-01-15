package org.MY_APP.main.repository;

import org.MY_APP.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    // ✅ LEADERBOARD QUERY
    List<User> findTop10ByOrderByTotalQuizScoreDesc();
}