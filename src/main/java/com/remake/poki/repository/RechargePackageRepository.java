package com.remake.poki.repository;

import com.remake.poki.model.RechargePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RechargePackageRepository extends JpaRepository<RechargePackage, Long> {

}
