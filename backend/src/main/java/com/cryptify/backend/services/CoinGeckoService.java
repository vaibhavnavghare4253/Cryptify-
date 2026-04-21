package com.cryptify.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.Map;
import java.util.List;

@Service
public class CoinGeckoService {
    
    @Autowired
    private RestTemplate restTemplate;

    public Object getMarketData() {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=50&page=1&sparkline=false";
        try {
            return restTemplate.getForObject(url, Object.class);
        } catch (Exception e) {
            return List.of();
        }
    }
    
    public BigDecimal getCoinPrice(String coinId) {
        String url = "https://api.coingecko.com/api/v3/simple/price?ids=" + coinId + "&vs_currencies=usd";
        try {
            Map<String, Map<String, Number>> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey(coinId)) {
                return new BigDecimal(response.get(coinId).get("usd").toString());
            }
        } catch (Exception e) {
             e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
}
