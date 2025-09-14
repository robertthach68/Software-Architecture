package com.example.checkingaccount.controller;

import com.example.checkingaccount.dto.AccountRequest;
import com.example.checkingaccount.dto.TransactionRequest;
import com.example.checkingaccount.model.CheckingAccount;
import com.example.checkingaccount.service.CheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/checking-accounts")
@CrossOrigin(origins = "*")
public class CheckingAccountController {

    @Autowired
    private CheckingAccountService checkingAccountService;

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountRequest request) {
        try {
            CheckingAccount account = checkingAccountService.createAccount(request);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating account: " + e.getMessage());
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getAccount(@PathVariable String accountNumber) {
        Optional<CheckingAccount> account = checkingAccountService.getAccount(accountNumber);
        if (account.isPresent()) {
            return ResponseEntity.ok(account.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CheckingAccount>> getAccountsByCustomer(@PathVariable String customerId) {
        List<CheckingAccount> accounts = checkingAccountService.getAccountsByCustomer(customerId);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionRequest request) {
        boolean success = checkingAccountService.deposit(request);
        if (success) {
            return ResponseEntity.ok("Deposit successful");
        } else {
            return ResponseEntity.badRequest().body("Deposit failed");
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody TransactionRequest request) {
        boolean success = checkingAccountService.withdraw(request);
        if (success) {
            return ResponseEntity.ok("Withdrawal successful");
        } else {
            return ResponseEntity.badRequest().body("Withdrawal failed");
        }
    }

    @PostMapping("/transfer-from")
    public ResponseEntity<?> transferFrom(@RequestBody TransactionRequest request) {
        boolean success = checkingAccountService.transferFrom(request.getAccountNumber(), request.getAmount());
        if (success) {
            return ResponseEntity.ok("Transfer from successful");
        } else {
            return ResponseEntity.badRequest().body("Transfer from failed");
        }
    }

    @PostMapping("/transfer-to")
    public ResponseEntity<?> transferTo(@RequestBody TransactionRequest request) {
        boolean success = checkingAccountService.transferTo(request.getAccountNumber(), request.getAmount());
        if (success) {
            return ResponseEntity.ok("Transfer to successful");
        } else {
            return ResponseEntity.badRequest().body("Transfer to failed");
        }
    }
}
