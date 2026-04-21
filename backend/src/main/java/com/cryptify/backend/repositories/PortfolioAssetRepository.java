package com.cryptify.backend.repositories;

import com.cryptify.backend.models.PortfolioAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioAssetRepository extends JpaRepository<PortfolioAsset, Long> {
    Optional<PortfolioAsset> findByPortfolioIdAndCoinId(Long portfolioId, String coinId);
}
