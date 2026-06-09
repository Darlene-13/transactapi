package io.github.darlene.transactapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    // email must be unique, two customers can't share an account
    @NotBlank @Email(message = "Provide a valid email")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    private LocalDateTime createdAt;
    // auto-set timestamp on first save
    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}