package io.github.darlene.transactapi.controller;

import io.github.darlene.transactapi.dto.request.CustomerRequest;
import io.github.darlene.transactapi.dto.response.ApiResponse;
import io.github.darlene.transactapi.dto.response.CustomerResponse;
import io.github.darlene.transactapi.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // Endpoint A — take customer details, persist them, open an account
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> registerCustomer(
            @Valid @RequestBody CustomerRequest request) {
        log.info("POST /customers — registering: {}", request.getEmail());
        CustomerResponse result = customerService.registerCustomer(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer registered successfully.", result));
    }
}