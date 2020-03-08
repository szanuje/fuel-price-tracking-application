package com.licencjat.max.paliwa.price;

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

    public void saveAll(List<Price> prices) {
        priceRepository.saveAll(prices);
    }

    public void deleteAll() {
        priceRepository.deleteAll();
    }

    public Iterable<Price> findByOrderByDatetimeDesc() {
        return priceRepository.findByOrderByTimestampDesc();
    }
}
