package com.cryptify.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId; 
    
    @Enumerated(EnumType.STRING)
    private TransactionType type; 
    
    @Column(nullable = false)
    private String coinId;
    
    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal amount; 
    
    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal price; 
    
    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
    
    public enum TransactionType {
        BUY, SELL
    }
}
