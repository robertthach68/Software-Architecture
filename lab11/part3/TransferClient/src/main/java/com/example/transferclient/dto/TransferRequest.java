package com.example.transferclient.dto;

import java.math.BigDecimal;

public class TransferRequest {
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private boolean simulateError;

    // Constructors
    public TransferRequest() {
    }

    public TransferRequest(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.simulateError = false;
    }

    public TransferRequest(String fromAccountNumber, String toAccountNumber, BigDecimal amount, boolean simulateError) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.simulateError = simulateError;
    }

    // Getters and Setters
    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isSimulateError() {
        return simulateError;
    }

    public void setSimulateError(boolean simulateError) {
        this.simulateError = simulateError;
    }
}
