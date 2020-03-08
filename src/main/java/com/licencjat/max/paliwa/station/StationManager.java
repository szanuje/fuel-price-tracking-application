package com.licencjat.max.paliwa.station;

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

    public void deleteById(Long id) {
        stationRepository.deleteById(id);
    }

    public Station save(Station s) {
        return stationRepository.save(s);
    }

    public void saveAll(List<Station> stations) {
        stationRepository.saveAll(stations);
    }

    public void deleteAll() {
        stationRepository.deleteAll();
    }

}
