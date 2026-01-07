package com.remake.poki.repository;

import com.remake.poki.model.UserMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMilestoneRepository extends JpaRepository<UserMilestone, Long> {
    

}
