package com.example.transferclient.feign;

import com.example.transferclient.dto.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "checking-account-service", url = "http://localhost:8082")
public interface CheckingAccountClient {

    @PostMapping("/api/checking-accounts/transfer-from")
    ResponseEntity<String> transferFrom(@RequestBody TransactionRequest request);

    @PostMapping("/api/checking-accounts/transfer-to")
    ResponseEntity<String> transferTo(@RequestBody TransactionRequest request);
}
