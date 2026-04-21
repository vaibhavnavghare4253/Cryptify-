package com.cryptify.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

@Entity
@Table(name = "portfolio_assets")
@Data
@NoArgsConstructor
public class PortfolioAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Portfolio portfolio;
    
    @Column(nullable = false)
    private String coinId; 
    
    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal quantity;
    
    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal averageBuyPrice;
}
