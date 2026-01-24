package com.remake.poki.repository;

import com.remake.poki.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findByUserId(Long userId);

    Optional<UserSession> findByUserIdAndIsActiveTrue(Long userId);


}