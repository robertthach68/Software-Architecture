package com.example.transferclient.controller;

import com.example.transferclient.dto.TransferRequest;
import com.example.transferclient.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@CrossOrigin(origins = "*")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping("/checking-to-saving")
    public ResponseEntity<String> transferFromCheckingToSaving(@RequestBody TransferRequest request) {
        String result = transferService.transferFromCheckingToSaving(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/saving-to-checking")
    public ResponseEntity<String> transferFromSavingToChecking(@RequestBody TransferRequest request) {
        String result = transferService.transferFromSavingToChecking(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/test-error")
    public ResponseEntity<String> testTransferWithError(@RequestBody TransferRequest request) {
        // Force error simulation
        request.setSimulateError(true);
        String result = transferService.transferFromCheckingToSaving(request);
        return ResponseEntity.ok(result);
    }
}
