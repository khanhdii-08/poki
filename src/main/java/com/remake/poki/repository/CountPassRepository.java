package com.remake.poki.repository;

import com.remake.poki.model.CountPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountPassRepository extends JpaRepository<CountPass, Long> {

}
