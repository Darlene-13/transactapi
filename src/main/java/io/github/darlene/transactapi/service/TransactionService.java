package io.github.darlene.transactapi.service;

import io.github.darlene.transactapi.dto.request.TransactionRequest;
import io.github.darlene.transactapi.dto.response.TransactionResponse;
import io.github.darlene.transactapi.entity.Account;
import io.github.darlene.transactapi.entity.Transaction;
import io.github.darlene.transactapi.exception.AccountNotFoundException;
import io.github.darlene.transactapi.exception.DuplicateTransferException;
import io.github.darlene.transactapi.exception.InsufficientFundsException;
import io.github.darlene.transactapi.repository.AccountRepository;
import io.github.darlene.transactapi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * API (b) — Transfers funds from one account to another,
     * updates both balances atomically.
     */
    @Transactional
    public TransactionResponse transferFunds(TransactionRequest req) {
        log.info("Transfer: {} → {} | amount={} | ref={}",
                req.getSenderAccountNumber(), req.getReceiverAccountNumber(),
                req.getAmount(), req.getTransferReference());

        // Reject duplicate submissions
        if (transactionRepository.existsByTransferReference(req.getTransferReference())) {
            throw new DuplicateTransferException(req.getTransferReference());
        }

        // Lock rows to prevent race conditions on concurrent transfers
        Account sender = accountRepository
                .findByAccountNumberWithLock(req.getSenderAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(req.getSenderAccountNumber()));

        Account receiver = accountRepository
                .findByAccountNumberWithLock(req.getReceiverAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(req.getReceiverAccountNumber()));

        if (sender.getAccountNumber().equals(receiver.getAccountNumber())) {
            throw new IllegalArgumentException("Sender and receiver cannot be the same account.");
        }

        if (sender.getBalance().compareTo(req.getAmount()) < 0) {
            throw new InsufficientFundsException(
                    sender.getAccountNumber(), sender.getBalance(), req.getAmount());
        }

        // Debit sender, credit receiver
        sender.setBalance(sender.getBalance().subtract(req.getAmount()));
        receiver.setBalance(receiver.getBalance().add(req.getAmount()));
        accountRepository.save(sender);
        accountRepository.save(receiver);

        // Persist transaction record
        Transaction tx = Transaction.builder()
                .senderAccount(sender)
                .receiverAccount(receiver)
                .amount(req.getAmount())
                .transferReference(req.getTransferReference())
                .status("SUCCESS")
                .description(req.getDescription())
                .build();
        tx = transactionRepository.save(tx);

        log.info("Transfer SUCCESS: ref={}", req.getTransferReference());

        return TransactionResponse.builder()
                .id(tx.getId())
                .transferReference(tx.getTransferReference())
                .senderAccount(tx.getSenderAccount().getAccountNumber())
                .receiverAccount(tx.getReceiverAccount().getAccountNumber())
                .amount(tx.getAmount())
                .status(tx.getStatus())
                .description(tx.getDescription())
                .transactedAt(tx.getTransactedAt())
                .build();
    }
}