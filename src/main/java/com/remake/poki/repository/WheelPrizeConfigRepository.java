package com.remake.poki.repository;

import com.remake.poki.model.WheelPrizeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WheelPrizeConfigRepository extends JpaRepository<WheelPrizeConfig, Long> {

}
