package com.cryptify.backend.controllers;

import com.cryptify.backend.services.TradingService;
import com.cryptify.backend.models.Transaction.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/trade")
@CrossOrigin(origins = "*")
public class TradingController {

    @Autowired
    private TradingService tradingService;

    @PostMapping("/execute")
    public ResponseEntity<?> executeTrade(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            String coinId = payload.get("coinId").toString();
            BigDecimal quantity = new BigDecimal(payload.get("quantity").toString());
            TransactionType type = TransactionType.valueOf(payload.get("type").toString());
            
            String result = tradingService.executeTrade(userId, coinId, quantity, type);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
