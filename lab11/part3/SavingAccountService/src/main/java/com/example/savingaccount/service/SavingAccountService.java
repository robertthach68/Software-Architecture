package com.example.savingaccount.service;

import com.example.savingaccount.dto.AccountRequest;
import com.example.savingaccount.dto.TransactionRequest;
import com.example.savingaccount.model.SavingAccount;
import com.example.savingaccount.repository.SavingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SavingAccountService {

    @Autowired
    private SavingAccountRepository savingAccountRepository;

    public SavingAccount createAccount(AccountRequest request) {
        if (savingAccountRepository.existsByAccountNumber(request.getAccountNumber())) {
            throw new RuntimeException("Account with number " + request.getAccountNumber() + " already exists");
        }

        SavingAccount account = new SavingAccount(
                request.getAccountNumber(),
                request.getCustomerId(),
                request.getInitialBalance() != null ? request.getInitialBalance() : BigDecimal.ZERO);

        return savingAccountRepository.save(account);
    }

    public Optional<SavingAccount> getAccount(String accountNumber) {
        return savingAccountRepository.findByAccountNumber(accountNumber);
    }

    public List<SavingAccount> getAccountsByCustomer(String customerId) {
        return savingAccountRepository.findByCustomerId(customerId);
    }

    @Transactional
    public boolean deposit(TransactionRequest request) {
        Optional<SavingAccount> accountOpt = savingAccountRepository.findByAccountNumber(request.getAccountNumber());
        if (accountOpt.isPresent()) {
            SavingAccount account = accountOpt.get();
            boolean success = account.deposit(request.getAmount());
            if (success) {
                savingAccountRepository.save(account);
            }
            return success;
        }
        return false;
    }

    @Transactional
    public boolean withdraw(TransactionRequest request) {
        Optional<SavingAccount> accountOpt = savingAccountRepository.findByAccountNumber(request.getAccountNumber());
        if (accountOpt.isPresent()) {
            SavingAccount account = accountOpt.get();
            boolean success = account.withdraw(request.getAmount());
            if (success) {
                savingAccountRepository.save(account);
            }
            return success;
        }
        return false;
    }

    @Transactional
    public boolean transferFrom(String accountNumber, BigDecimal amount) {
        Optional<SavingAccount> accountOpt = savingAccountRepository.findByAccountNumber(accountNumber);
        if (accountOpt.isPresent()) {
            SavingAccount account = accountOpt.get();
            boolean success = account.withdraw(amount);
            if (success) {
                savingAccountRepository.save(account);
            }
            return success;
        }
        return false;
    }

    @Transactional
    public boolean transferTo(String accountNumber, BigDecimal amount) {
        Optional<SavingAccount> accountOpt = savingAccountRepository.findByAccountNumber(accountNumber);
        if (accountOpt.isPresent()) {
            SavingAccount account = accountOpt.get();
            boolean success = account.deposit(amount);
            if (success) {
                savingAccountRepository.save(account);
            }
            return success;
        }
        return false;
    }
}
