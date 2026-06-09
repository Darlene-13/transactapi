package io.github.darlene.transactapi.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class BalanceResponse {
    private String accountNumber;
    private String customerName;
    private BigDecimal balance;
    private String currency;
    private LocalDateTime asOf;
}