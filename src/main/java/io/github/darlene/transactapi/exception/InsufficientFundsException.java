package io.github.darlene.transactapi.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String accountNumber, BigDecimal available, BigDecimal requested) {
        super(String.format("Account %s has insufficient funds. Available: %.2f, Requested: %.2f",
                accountNumber, available, requested));
    }
}