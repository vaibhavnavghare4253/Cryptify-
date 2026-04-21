package com.cryptify.backend.controllers;

import com.cryptify.backend.services.CoinGeckoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/market")
@CrossOrigin(origins = "*")
public class MarketController {

    @Autowired
    private CoinGeckoService coinGeckoService;

    @GetMapping
    public Object getMarket() {
        return coinGeckoService.getMarketData();
    }
}
