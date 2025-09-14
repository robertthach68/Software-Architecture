package com.example.checkingaccount.repository;

import com.example.checkingaccount.model.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Long> {

    Optional<CheckingAccount> findByAccountNumber(String accountNumber);

    List<CheckingAccount> findByCustomerId(String customerId);

    boolean existsByAccountNumber(String accountNumber);
}
