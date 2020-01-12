package com.licencjat.max.paliwa.station;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @EventListener(ApplicationReadyEvent.class)
    public void initStations() {
        save(new Station(1L, "Orlen", "Kapelanka 2", 30316, new BigDecimal("31.23232"), new BigDecimal("22.32312312")));
        save(new Station(2L, "BP", "Ulica 2", 30316, new BigDecimal("31.23232"), new BigDecimal("22.32312312")));
        save(new Station(3L, "Lotos", "Ulica 3", 30316, new BigDecimal("31.23232"), new BigDecimal("22.32312312")));
    }
}
