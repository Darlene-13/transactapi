package io.github.darlene.transactapi.service;

import io.github.darlene.transactapi.dto.request.CustomerRequest;
import io.github.darlene.transactapi.dto.response.TransactionResponse;
import io.github.darlene.transactapi.entity.Account;
import io.github.darlene.transactapi.entity.Customer;
import io.github.darlene.transactapi.repository.AccountRepository;
import io.github.darlene.transactapi.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    /**
     * API (a) — Saves customer details + opens an account for them.
     */
    @Transactional
    public TransactionResponse registerCustomer(CustomerRequest req) {
        log.info("Registering customer: email={}", req.getEmail());

        if (customerRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException(
                    "Customer with email " + req.getEmail() + " already exists.");
        }

        Customer customer = Customer.builder()
                .name(req.getName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .build();
        customer = customerRepository.save(customer);

        String accountNumber = "ACC" + UUID.randomUUID().toString()
                .replaceAll("[^0-9]", "").substring(0, 10);

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .customer(customer)
                .balance(req.getInitialBalance())
                .currency("KES")
                .build();
        accountRepository.save(account);

        log.info("Customer id={} created, account={}", customer.getId(), accountNumber);

        return TransactionResponse.builder()
                .transferReference(accountNumber)
                .senderAccount("N/A")
                .receiverAccount(accountNumber)
                .amount(req.getInitialBalance())
                .status("ACCOUNT_CREATED")
                .description("Welcome " + customer.getName() + "! Account: " + accountNumber)
                .transactedAt(LocalDateTime.now())
                .build();
    }
}