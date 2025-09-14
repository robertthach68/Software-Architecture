package com.example.checkingaccount.service;

import com.example.checkingaccount.dto.AccountRequest;
import com.example.checkingaccount.dto.TransactionRequest;
import com.example.checkingaccount.model.CheckingAccount;
import com.example.checkingaccount.repository.CheckingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CheckingAccountService {

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    public CheckingAccount createAccount(AccountRequest request) {
        if (checkingAccountRepository.existsByAccountNumber(request.getAccountNumber())) {
            throw new RuntimeException("Account with number " + request.getAccountNumber() + " already exists");
        }

        CheckingAccount account = new CheckingAccount(
                request.getAccountNumber(),
                request.getCustomerId(),
                request.getInitialBalance() != null ? request.getInitialBalance() : BigDecimal.ZERO);

        return checkingAccountRepository.save(account);
    }

    public Optional<CheckingAccount> getAccount(String accountNumber) {
        return checkingAccountRepository.findByAccountNumber(accountNumber);
    }

    public List<CheckingAccount> getAccountsByCustomer(String customerId) {
        return checkingAccountRepository.findByCustomerId(customerId);
    }

    @Transactional
    public boolean deposit(TransactionRequest request) {
        Optional<CheckingAccount> accountOpt = checkingAccountRepository
                .findByAccountNumber(request.getAccountNumber());
        if (accountOpt.isPresent()) {
            CheckingAccount account = accountOpt.get();
            boolean success = account.deposit(request.getAmount());
            if (success) {
                checkingAccountRepository.save(account);
            }
            return success;
        }
        return false;
    }

    @Transactional
    public boolean withdraw(TransactionRequest request) {
        Optional<CheckingAccount> accountOpt = checkingAccountRepository
                .findByAccountNumber(request.getAccountNumber());
        if (accountOpt.isPresent()) {
            CheckingAccount account = accountOpt.get();
            boolean success = account.withdraw(request.getAmount());
            if (success) {
                checkingAccountRepository.save(account);
            }
            return success;
        }
        return false;
    }

    @Transactional
    public boolean transferFrom(String accountNumber, BigDecimal amount) {
        Optional<CheckingAccount> accountOpt = checkingAccountRepository.findByAccountNumber(accountNumber);
        if (accountOpt.isPresent()) {
            CheckingAccount account = accountOpt.get();
            boolean success = account.withdraw(amount);
            if (success) {
                checkingAccountRepository.save(account);
            }
            return success;
        }
        return false;
    }

    @Transactional
    public boolean transferTo(String accountNumber, BigDecimal amount) {
        Optional<CheckingAccount> accountOpt = checkingAccountRepository.findByAccountNumber(accountNumber);
        if (accountOpt.isPresent()) {
            CheckingAccount account = accountOpt.get();
            boolean success = account.deposit(amount);
            if (success) {
                checkingAccountRepository.save(account);
            }
            return success;
        }
        return false;
    }
}
