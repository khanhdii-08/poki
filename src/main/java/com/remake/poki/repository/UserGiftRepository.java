package com.remake.poki.repository;

import com.remake.poki.model.UserGift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGiftRepository extends JpaRepository<UserGift, Long> {

}