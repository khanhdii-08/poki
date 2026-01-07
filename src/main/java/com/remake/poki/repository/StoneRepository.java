package com.remake.poki.repository;

import com.remake.poki.model.Stone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoneRepository extends JpaRepository<Stone, Long> {


}
