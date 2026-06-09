package io.github.darlene.transactapi.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CustomerRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank
    @Email(message = "Provide a valid email")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotNull
    @DecimalMin(value = "0.0", message = "Initial balance cannot be negative")
    private BigDecimal initialBalance;
}