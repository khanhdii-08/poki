package com.remake.poki.repository;

import com.remake.poki.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingRepository extends JpaRepository<User, Long> {
    

}
