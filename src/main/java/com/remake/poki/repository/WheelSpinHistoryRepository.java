package com.remake.poki.repository;

import com.remake.poki.model.WheelSpinHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WheelSpinHistoryRepository extends JpaRepository<WheelSpinHistory, Long> {

}
