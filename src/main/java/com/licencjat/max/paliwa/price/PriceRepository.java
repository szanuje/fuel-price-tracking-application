package com.licencjat.max.paliwa.price;

import org.springframework.data.repository.CrudRepository;

public interface PriceRepository extends CrudRepository<Price, Long> {

    Iterable<Price> findByOrderByTimestampDesc();
}
