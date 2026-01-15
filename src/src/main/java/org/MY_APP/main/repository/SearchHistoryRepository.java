package org.MY_APP.main.repository;

import jakarta.transaction.Transactional;
import org.MY_APP.main.model.SearchHistory;
import org.MY_APP.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    List<SearchHistory> findByUserOrderBySearchedAtDesc(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM SearchHistory h WHERE h.user = :user")
    void clearByUser(@Param("user") User user);
}
