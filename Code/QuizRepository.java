package org.MY_APP.main.repository;

import org.MY_APP.main.model.Quiz;
import org.MY_APP.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findByUserOrderByCreatedAtDesc(User user);
}
