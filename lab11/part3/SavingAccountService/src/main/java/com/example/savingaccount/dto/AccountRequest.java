package com.example.savingaccount.dto;

import java.math.BigDecimal;

public class AccountRequest {
    private String accountNumber;
    private String customerId;
    private BigDecimal initialBalance;

    // Constructors
    public AccountRequest() {
    }

    public AccountRequest(String accountNumber, String customerId, BigDecimal initialBalance) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.initialBalance = initialBalance;
    }

    // Getters and Setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}
