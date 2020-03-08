package com.licencjat.max.paliwa.station;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stations")
public class StationController {

    private StationManager stationManager;

    @Autowired
    public StationController(StationManager stationManager) {
        this.stationManager = stationManager;
    }

    @GetMapping("/all")
    public String getAllStations(Model model) {
        Iterable<Station> stations = stationManager.findAll();
        model.addAttribute("stations", stations);
        return "stations";
    }

    @GetMapping("/{id}")
    public Station getStation(@PathVariable Long id) {
        return stationManager.findById(id).orElse(null);
    }

    @PostMapping
    public Station addStation(@RequestBody Station station) {
        return stationManager.save(station);
    }

    @PutMapping
    public Station updateStation(@RequestBody Station station) {
        return stationManager.save(station);
    }

    @DeleteMapping
    public void deleteStation(@PathVariable Long id) {
        stationManager.deleteById(id);
    }
}
