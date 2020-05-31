package com.licencjat.max.fuel.station;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StationRepository
        extends CrudRepository<Station, Long> {

    Optional<Station> findByCityAndStreetAndPostalCode(
            String city, String street, String postalCode);
}
