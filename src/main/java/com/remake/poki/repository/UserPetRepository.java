package com.remake.poki.repository;

import com.remake.poki.model.UserPet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPetRepository extends JpaRepository<UserPet, Long> {


}