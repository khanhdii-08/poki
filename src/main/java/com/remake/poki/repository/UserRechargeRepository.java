package com.remake.poki.repository;

import com.remake.poki.model.UserRecharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRechargeRepository extends JpaRepository<UserRecharge, Long> {


}