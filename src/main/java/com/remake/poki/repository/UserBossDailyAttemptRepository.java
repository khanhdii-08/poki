package com.remake.poki.repository;

import com.remake.poki.model.UserBossDailyAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserBossDailyAttemptRepository extends JpaRepository<UserBossDailyAttempt, Long> {

}