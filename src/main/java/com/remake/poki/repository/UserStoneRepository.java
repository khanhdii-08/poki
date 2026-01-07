package com.remake.poki.repository;

import com.remake.poki.model.StoneUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStoneRepository extends JpaRepository<StoneUser, Long> {

}
