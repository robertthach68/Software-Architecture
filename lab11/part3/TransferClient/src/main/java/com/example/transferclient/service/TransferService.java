package com.example.transferclient.service;

import com.example.transferclient.dto.TransferRequest;
import com.example.transferclient.dto.TransactionRequest;
import com.example.transferclient.feign.CheckingAccountClient;
import com.example.transferclient.feign.SavingAccountClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    @Autowired
    private CheckingAccountClient checkingAccountClient;

    @Autowired
    private SavingAccountClient savingAccountClient;

    /**
     * Transfer money from checking account to saving account
     * This method implements a distributed transaction pattern
     */
    public String transferFromCheckingToSaving(TransferRequest request) {
        try {
            // Step 1: Withdraw from checking account
            TransactionRequest withdrawRequest = new TransactionRequest(
                    request.getFromAccountNumber(),
                    request.getAmount());

            var withdrawResponse = checkingAccountClient.transferFrom(withdrawRequest);
            if (!withdrawResponse.getStatusCode().is2xxSuccessful()) {
                return "Failed to withdraw from checking account: " + withdrawResponse.getBody();
            }

            // Simulate error if requested (for testing transaction rollback)
            if (request.isSimulateError()) {
                throw new RuntimeException("Simulated error during transfer - testing rollback");
            }

            // Step 2: Deposit to saving account
            TransactionRequest depositRequest = new TransactionRequest(
                    request.getToAccountNumber(),
                    request.getAmount());

            var depositResponse = savingAccountClient.transferTo(depositRequest);
            if (!depositResponse.getStatusCode().is2xxSuccessful()) {
                // If deposit fails, we need to rollback the withdrawal
                // This is a simplified rollback - in a real scenario, you'd use Saga pattern or
                // 2PC
                TransactionRequest rollbackRequest = new TransactionRequest(
                        request.getFromAccountNumber(),
                        request.getAmount());
                checkingAccountClient.transferTo(rollbackRequest);
                return "Transfer failed during deposit. Withdrawal has been rolled back.";
            }

            return "Transfer completed successfully: $" + request.getAmount() +
                    " transferred from checking account " + request.getFromAccountNumber() +
                    " to saving account " + request.getToAccountNumber();

        } catch (Exception e) {
            // If any error occurs, attempt to rollback the withdrawal
            try {
                TransactionRequest rollbackRequest = new TransactionRequest(
                        request.getFromAccountNumber(),
                        request.getAmount());
                checkingAccountClient.transferTo(rollbackRequest);
                return "Transfer failed: " + e.getMessage() + ". Withdrawal has been rolled back.";
            } catch (Exception rollbackException) {
                return "Transfer failed: " + e.getMessage() +
                        ". Rollback also failed: " + rollbackException.getMessage() +
                        ". Manual intervention required!";
            }
        }
    }

    /**
     * Transfer money from saving account to checking account
     */
    public String transferFromSavingToChecking(TransferRequest request) {
        try {
            // Step 1: Withdraw from saving account
            TransactionRequest withdrawRequest = new TransactionRequest(
                    request.getFromAccountNumber(),
                    request.getAmount());

            var withdrawResponse = savingAccountClient.transferFrom(withdrawRequest);
            if (!withdrawResponse.getStatusCode().is2xxSuccessful()) {
                return "Failed to withdraw from saving account: " + withdrawResponse.getBody();
            }

            // Simulate error if requested
            if (request.isSimulateError()) {
                throw new RuntimeException("Simulated error during transfer - testing rollback");
            }

            // Step 2: Deposit to checking account
            TransactionRequest depositRequest = new TransactionRequest(
                    request.getToAccountNumber(),
                    request.getAmount());

            var depositResponse = checkingAccountClient.transferTo(depositRequest);
            if (!depositResponse.getStatusCode().is2xxSuccessful()) {
                // Rollback withdrawal
                TransactionRequest rollbackRequest = new TransactionRequest(
                        request.getFromAccountNumber(),
                        request.getAmount());
                savingAccountClient.transferTo(rollbackRequest);
                return "Transfer failed during deposit. Withdrawal has been rolled back.";
            }

            return "Transfer completed successfully: $" + request.getAmount() +
                    " transferred from saving account " + request.getFromAccountNumber() +
                    " to checking account " + request.getToAccountNumber();

        } catch (Exception e) {
            // Rollback withdrawal
            try {
                TransactionRequest rollbackRequest = new TransactionRequest(
                        request.getFromAccountNumber(),
                        request.getAmount());
                savingAccountClient.transferTo(rollbackRequest);
                return "Transfer failed: " + e.getMessage() + ". Withdrawal has been rolled back.";
            } catch (Exception rollbackException) {
                return "Transfer failed: " + e.getMessage() +
                        ". Rollback also failed: " + rollbackException.getMessage() +
                        ". Manual intervention required!";
            }
        }
    }
}
