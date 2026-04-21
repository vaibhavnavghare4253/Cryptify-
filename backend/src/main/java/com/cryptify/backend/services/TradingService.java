package com.cryptify.backend.services;

import com.cryptify.backend.models.*;
import com.cryptify.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TradingService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PortfolioRepository portfolioRepository;
    
    @Autowired
    private PortfolioAssetRepository assetRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private CoinGeckoService coinGeckoService;

    @Transactional
    public String executeTrade(Long userId, String coinId, BigDecimal quantity, Transaction.TransactionType type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        Portfolio portfolio = portfolioRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Portfolio p = new Portfolio();
                    p.setUser(user);
                    return portfolioRepository.save(p);
                });

        BigDecimal currentPrice = coinGeckoService.getCoinPrice(coinId);
        if (currentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Could not fetch valid price for " + coinId);
        }
        
        BigDecimal totalCost = currentPrice.multiply(quantity).setScale(2, RoundingMode.HALF_UP);

        if (type == Transaction.TransactionType.BUY) {
            if (user.getBalance().compareTo(totalCost) < 0) {
                throw new RuntimeException("Insufficient fiat balance");
            }
            user.setBalance(user.getBalance().subtract(totalCost));
            
            PortfolioAsset asset = assetRepository.findByPortfolioIdAndCoinId(portfolio.getId(), coinId)
                    .orElse(new PortfolioAsset());
            
            if (asset.getId() == null) {
                asset.setPortfolio(portfolio);
                asset.setCoinId(coinId);
                asset.setQuantity(quantity);
                asset.setAverageBuyPrice(currentPrice);
            } else {
                BigDecimal oldTotal = asset.getQuantity().multiply(asset.getAverageBuyPrice());
                BigDecimal newTotal = oldTotal.add(totalCost);
                BigDecimal newQuantity = asset.getQuantity().add(quantity);
                asset.setAverageBuyPrice(newTotal.divide(newQuantity, 8, RoundingMode.HALF_UP));
                asset.setQuantity(newQuantity);
            }
            assetRepository.save(asset);
            
        } else if (type == Transaction.TransactionType.SELL) {
            PortfolioAsset asset = assetRepository.findByPortfolioIdAndCoinId(portfolio.getId(), coinId)
                    .orElseThrow(() -> new RuntimeException("Asset not found in portfolio"));
                    
            if (asset.getQuantity().compareTo(quantity) < 0) {
                throw new RuntimeException("Insufficient crypto balance");
            }
            
            asset.setQuantity(asset.getQuantity().subtract(quantity));
            if (asset.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                assetRepository.delete(asset);
            } else {
                assetRepository.save(asset);
            }
            
            user.setBalance(user.getBalance().add(totalCost));
        }

        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setCoinId(coinId);
        transaction.setAmount(quantity);
        transaction.setPrice(currentPrice);
        transaction.setType(type);
        transactionRepository.save(transaction);
        
        return "Trade executed successfully";
    }
}
