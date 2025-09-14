package com.example.transferclient.dto;

import java.math.BigDecimal;

public class TransactionRequest {
    private String accountNumber;
    private BigDecimal amount;

    // Constructors
    public TransactionRequest() {
    }

    public TransactionRequest(String accountNumber, BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    // Getters and Setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
