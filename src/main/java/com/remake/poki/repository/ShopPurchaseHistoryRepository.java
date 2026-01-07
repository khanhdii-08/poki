package com.remake.poki.repository;

import com.remake.poki.model.ShopPurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopPurchaseHistoryRepository extends JpaRepository<ShopPurchaseHistory, Long> {

}
