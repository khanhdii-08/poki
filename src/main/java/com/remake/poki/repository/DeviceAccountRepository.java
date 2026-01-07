package com.remake.poki.repository;

import com.remake.poki.model.DeviceAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceAccountRepository extends JpaRepository<DeviceAccount, Long> {

}
