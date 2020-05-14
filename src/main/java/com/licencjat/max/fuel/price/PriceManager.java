package com.licencjat.max.fuel.price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceManager {

    private PriceRepository priceRepository;

    @Autowired
    public PriceManager(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public Price save(Price price) {
        return priceRepository.save(price);
    }

    public Iterable<Price> saveAll(List<Price> prices) {
        return priceRepository.saveAll(prices);
    }

    public void deleteAll() {
        priceRepository.deleteAll();
    }
}
