package io.github.darlene.transactapi.service;

import io.github.darlene.transactapi.dto.response.BalanceResponse;
import io.github.darlene.transactapi.entity.Account;
import io.github.darlene.transactapi.exception.AccountNotFoundException;
import io.github.darlene.transactapi.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public BalanceResponse getBalance(String accountNumber) {
        log.info("Balance enquiry: account={}", accountNumber);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        return BalanceResponse.builder()
                .accountNumber(account.getAccountNumber())
                .customerName(account.getCustomer().getName())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .asOf(LocalDateTime.now())
                .build();
    }
}