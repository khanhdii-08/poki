package com.remake.poki.repository;

import com.remake.poki.model.UserPackagePurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPackagePurchaseRepository extends JpaRepository<UserPackagePurchase, Long> {

}
