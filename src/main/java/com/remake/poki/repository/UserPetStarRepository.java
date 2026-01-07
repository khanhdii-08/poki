package com.remake.poki.repository;

import com.remake.poki.model.UserPetStar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPetStarRepository extends JpaRepository<UserPetStar, Long> {

}
