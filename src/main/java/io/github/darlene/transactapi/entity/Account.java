package io.github.darlene.transactapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor  // Safe fallback required by JPA
@AllArgsConstructor // Required internally by Lombok's @Builder pattern
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Column(nullable = false)
    @Builder.Default // Clears the builder compilation warning
    private String currency = "KES";

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Custom constructor for manual instantiation if used elsewhere
    public Account(String accountNumber, Customer customer, BigDecimal balance, String currency) {
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = balance;
        if (currency != null) {
            this.currency = currency;
        }
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.from(LocalDateTime.now());
    }
}