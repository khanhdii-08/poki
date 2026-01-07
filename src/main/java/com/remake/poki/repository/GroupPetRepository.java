package com.remake.poki.repository;

import com.remake.poki.model.GroupPet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupPetRepository extends JpaRepository<GroupPet, Long> {


}
