package com.remake.poki.repository;

import com.remake.poki.model.RechargeMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RechargeMilestoneRepository extends JpaRepository<RechargeMilestone, Long> {


}
