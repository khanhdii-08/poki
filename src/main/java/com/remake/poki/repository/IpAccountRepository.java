package com.remake.poki.repository;

import com.remake.poki.model.IpAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpAccountRepository extends JpaRepository<IpAccount, Long> {

}