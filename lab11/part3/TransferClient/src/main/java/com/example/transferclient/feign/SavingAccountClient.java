package com.example.transferclient.feign;

import com.example.transferclient.dto.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "saving-account-service", url = "http://localhost:8081")
public interface SavingAccountClient {

    @PostMapping("/api/saving-accounts/transfer-from")
    ResponseEntity<String> transferFrom(@RequestBody TransactionRequest request);

    @PostMapping("/api/saving-accounts/transfer-to")
    ResponseEntity<String> transferTo(@RequestBody TransactionRequest request);
}
