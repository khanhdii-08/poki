package com.remake.poki.repository;

import com.remake.poki.model.GiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftCodeRepository extends JpaRepository<GiftCode, Long> {

}