package com.remake.poki.repository;

import com.remake.poki.model.PetStarSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetStarSlotRepository extends JpaRepository<PetStarSlot, Long> {

}

