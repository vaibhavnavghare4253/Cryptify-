package com.cryptify.backend.config;

import com.cryptify.backend.models.User;
import com.cryptify.backend.models.Portfolio;
import com.cryptify.backend.repositories.UserRepository;
import com.cryptify.backend.repositories.PortfolioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class StartupRunner {

    @Bean
    public CommandLineRunner run(UserRepository userRepository, PortfolioRepository portfolioRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User user = new User();
                user.setUsername("testuser");
                user.setEmail("testuser@cryptify.com");
                user.setBalance(new BigDecimal("10000.00"));
                userRepository.save(user);
                
                Portfolio portfolio = new Portfolio();
                portfolio.setUser(user);
                portfolioRepository.save(portfolio);
                
                System.out.println("Created default test user with $10,000 balance.");
            }
        };
    }
}
