package io.github.darlene.transactapi.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class CustomerResponse {
    private Long customerId;
    private String name;
    private String email;
    private String phone;
    private String accountNumber;
    private BigDecimal balance;
    private String currency;
    private LocalDateTime createdAt;
}