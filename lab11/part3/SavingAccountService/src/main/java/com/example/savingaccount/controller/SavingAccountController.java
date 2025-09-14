package com.example.savingaccount.controller;

import com.example.savingaccount.dto.AccountRequest;
import com.example.savingaccount.dto.TransactionRequest;
import com.example.savingaccount.model.SavingAccount;
import com.example.savingaccount.service.SavingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/saving-accounts")
@CrossOrigin(origins = "*")
public class SavingAccountController {

    @Autowired
    private SavingAccountService savingAccountService;

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountRequest request) {
        try {
            SavingAccount account = savingAccountService.createAccount(request);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating account: " + e.getMessage());
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getAccount(@PathVariable String accountNumber) {
        Optional<SavingAccount> account = savingAccountService.getAccount(accountNumber);
        if (account.isPresent()) {
            return ResponseEntity.ok(account.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SavingAccount>> getAccountsByCustomer(@PathVariable String customerId) {
        List<SavingAccount> accounts = savingAccountService.getAccountsByCustomer(customerId);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionRequest request) {
        boolean success = savingAccountService.deposit(request);
        if (success) {
            return ResponseEntity.ok("Deposit successful");
        } else {
            return ResponseEntity.badRequest().body("Deposit failed");
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody TransactionRequest request) {
        boolean success = savingAccountService.withdraw(request);
        if (success) {
            return ResponseEntity.ok("Withdrawal successful");
        } else {
            return ResponseEntity.badRequest().body("Withdrawal failed");
        }
    }

    @PostMapping("/transfer-from")
    public ResponseEntity<?> transferFrom(@RequestBody TransactionRequest request) {
        boolean success = savingAccountService.transferFrom(request.getAccountNumber(), request.getAmount());
        if (success) {
            return ResponseEntity.ok("Transfer from successful");
        } else {
            return ResponseEntity.badRequest().body("Transfer from failed");
        }
    }

    @PostMapping("/transfer-to")
    public ResponseEntity<?> transferTo(@RequestBody TransactionRequest request) {
        boolean success = savingAccountService.transferTo(request.getAccountNumber(), request.getAmount());
        if (success) {
            return ResponseEntity.ok("Transfer to successful");
        } else {
            return ResponseEntity.badRequest().body("Transfer to failed");
        }
    }
}
