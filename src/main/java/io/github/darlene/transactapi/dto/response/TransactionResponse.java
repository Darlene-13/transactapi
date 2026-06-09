package io.github.darlene.transactapi.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionResponse {
    private Long id;
    private String transferReference;
    private String senderAccount;
    private String receiverAccount;
    private BigDecimal amount;
    private String status;
    private String description;
    private LocalDateTime transactedAt;
}