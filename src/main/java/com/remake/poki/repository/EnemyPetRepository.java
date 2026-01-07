package com.remake.poki.repository;

import com.remake.poki.model.EnemyPet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnemyPetRepository extends JpaRepository<EnemyPet, Long> {

}
