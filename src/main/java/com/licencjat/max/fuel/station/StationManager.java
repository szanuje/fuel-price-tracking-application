package com.licencjat.max.fuel.station;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StationManager {

    private StationRepository stationRepository;

    @Autowired
    public StationManager(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Optional<Station> findById(Long id) {
        return stationRepository.findById(id);
    }

    public Iterable<Station> findAll() {
        return stationRepository.findAll();
    }

    public Station save(Station s) {
        return stationRepository.save(s);
    }

    public Iterable<Station> saveAll(List<Station> stations) {
        return stationRepository.saveAll(stations);
    }

    public void deleteAll() {
        stationRepository.deleteAll();
    }

    public Optional<Station> findByCityAndStreetAndPostalCode(String city, String street, String postalCode) {
        return stationRepository.findByCityAndStreetAndPostalCode(city, street, postalCode);
    }

}
