package com.example.savingaccount.repository;

import com.example.savingaccount.model.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingAccountRepository extends JpaRepository<SavingAccount, Long> {

    Optional<SavingAccount> findByAccountNumber(String accountNumber);

    List<SavingAccount> findByCustomerId(String customerId);

    boolean existsByAccountNumber(String accountNumber);
}
