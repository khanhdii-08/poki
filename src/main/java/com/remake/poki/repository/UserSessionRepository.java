package com.remake.poki.repository;

import com.remake.poki.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    @Modifying
    @Query("DELETE FROM UserSession s WHERE s.userId = :userId")
    int deleteUserSessionByUserId(Long userId);
}