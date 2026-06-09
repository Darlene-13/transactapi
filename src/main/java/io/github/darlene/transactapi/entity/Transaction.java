package io.github.darlene.transactapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //account that sent the money
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_account_id", nullable = false)
    private Account senderAccount;

    // Receiving account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_account_id", nullable = false)
    private Account receiverAccount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    // client sends this, we reject duplicates
    @Column(unique = true)
    private String transferReference;

    @Column(nullable = false)
    private String status; // PENDING, SUCCESS, FAILED

    private String description;

    private LocalDateTime transactedAt;

    @PrePersist
    void onCreate() {
        this.transactedAt = LocalDateTime.now();
    }
}