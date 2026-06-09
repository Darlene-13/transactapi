package io.github.darlene.transactapi.controller;

import io.github.darlene.transactapi.dto.response.ApiResponse;
import io.github.darlene.transactapi.dto.response.BalanceResponse;
import io.github.darlene.transactapi.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * GET /api/v1/accounts/{accountNumber}/balance
     * Returns current balance in a well-formatted, user-friendly response.
     */
    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<ApiResponse<BalanceResponse>> getBalance(
            @PathVariable String accountNumber) {
        log.info("GET /accounts/{}/balance", accountNumber);
        BalanceResponse result = accountService.getBalance(accountNumber);
        return ResponseEntity.ok(ApiResponse.success("Balance retrieved successfully.", result));
    }
}