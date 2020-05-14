package com.licencjat.max.fuel.station;

import com.licencjat.max.fuel.security.AuthenticationConstants;
import com.licencjat.max.fuel.utils.Haversine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(AuthenticationConstants.GET_MARKERS_ENDPOINT)
public class MarkersForMapRestController {

    StationManager stationManager;

    public MarkersForMapRestController(StationManager stationManager) {
        this.stationManager = stationManager;
    }

    @GetMapping
    public Iterable<Station> getMarkers(@RequestParam BigDecimal lat, @RequestParam BigDecimal lon, @RequestParam int distance) {
        Iterable<Station> stations = stationManager.findAll();
        List<Station> results = new ArrayList<>();
        stations.forEach(station -> {
            if (Haversine.measure(
                    lat.doubleValue(), lon.doubleValue(),
                    station.getLat().doubleValue(), station.getLon().doubleValue()
            ) < distance) {
                results.add(station);
            }
        });
        return results;
    }
}
