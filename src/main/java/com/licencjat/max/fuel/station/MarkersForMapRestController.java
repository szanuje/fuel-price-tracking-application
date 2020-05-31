package com.licencjat.max.fuel.station;

import com.licencjat.max.fuel.security.AuthenticationConstants;
import com.licencjat.max.fuel.utils.Haversine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(AuthenticationConstants.GET_MARKERS_ENDPOINT)
public class MarkersForMapRestController {

    StationManager stationManager;

    @Autowired
    public MarkersForMapRestController(StationManager stationManager) {
        this.stationManager = stationManager;
    }

    @GetMapping
    public ResponseEntity<Iterable<Station>> getMarkers(@RequestParam BigDecimal lat,
                                                        @RequestParam BigDecimal lon,
                                                        @RequestParam int distance) {
        List<Station> results = StreamSupport
                .stream(stationManager.findAll().spliterator(), false)
                .filter(station -> Haversine.measure(lat.doubleValue(), lon.doubleValue(),
                        station.getLat().doubleValue(), station.getLon().doubleValue())
                        < distance).collect(Collectors.toList());
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
