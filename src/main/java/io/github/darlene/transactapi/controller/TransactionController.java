package io.github.darlene.transactapi.controller;

import io.github.darlene.transactapi.dto.request.TransactionRequest;
import io.github.darlene.transactapi.dto.response.ApiResponse;
import io.github.darlene.transactapi.dto.response.TransactionResponse;
import io.github.darlene.transactapi.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * POST /api/v1/transactions/transfer
     * Transfers funds from sender to receiver and updates both account balances.
     */
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(
            @Valid @RequestBody TransactionRequest request) {
        log.info("POST /transactions/transfer — ref={}", request.getTransferReference());
        TransactionResponse result = transactionService.transferFunds(request);
        return ResponseEntity.ok(ApiResponse.success("Transfer completed successfully.", result));
    }
}