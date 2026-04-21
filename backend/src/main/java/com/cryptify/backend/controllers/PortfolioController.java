package com.cryptify.backend.controllers;

import com.cryptify.backend.models.User;
import com.cryptify.backend.models.Portfolio;
import com.cryptify.backend.models.Transaction;
import com.cryptify.backend.repositories.UserRepository;
import com.cryptify.backend.repositories.PortfolioRepository;
import com.cryptify.backend.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getPortfolio(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        Portfolio portfolio = portfolioRepository.findByUserId(userId).orElse(null);
        return ResponseEntity.ok(Map.of(
            "user", user,
            "portfolio", portfolio != null ? portfolio : Map.of("assets", List.of())
        ));
    }
    
    @GetMapping("/{userId}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionRepository.findByUserIdOrderByTimestampDesc(userId));
    }
}
