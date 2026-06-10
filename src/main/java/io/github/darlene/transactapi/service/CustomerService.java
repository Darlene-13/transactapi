package io.github.darlene.transactapi.service;

import io.github.darlene.transactapi.dto.request.CustomerRequest;
import io.github.darlene.transactapi.dto.response.CustomerResponse;
import io.github.darlene.transactapi.entity.Account;
import io.github.darlene.transactapi.entity.Customer;
import io.github.darlene.transactapi.repository.AccountRepository;
import io.github.darlene.transactapi.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public CustomerResponse registerCustomer(CustomerRequest req) {
        log.info("Registering customer: email={}", req.getEmail());

        // no two customers should share an email — reject early before hitting the DB
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

        // strip non-numeric chars from UUID to get a clean numeric account number
        String accountNumber = "ACC" + UUID.randomUUID().toString()
                .replaceAll("[^0-9]", "").substring(0, 10);

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .customer(customer)
                .balance(req.getInitialBalance())
                .currency("KES")
                .build();

        accountRepository.save(account);
        log.info("Customer id={} created with account={}", customer.getId(), accountNumber);

        // return what the client actually cares about — who they are and their account details
        return CustomerResponse.builder()
                .customerId(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .accountNumber(accountNumber)
                .balance(req.getInitialBalance())
                .currency("KES")
                .createdAt(customer.getCreatedAt())
                .build();
    }
}