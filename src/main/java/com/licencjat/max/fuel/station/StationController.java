package com.licencjat.max.fuel.station;

import com.licencjat.max.fuel.exceptions.StationAlreadyExistsException;
import com.licencjat.max.fuel.security.AuthenticationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StationController {

    private StationManager stationManager;

    @Autowired
    public StationController(StationManager stationManager) {
        this.stationManager = stationManager;
    }

    @GetMapping(AuthenticationConstants.ADD_STATION_ENDPOINT)
    public String addStationGET(Model model) {
        model.addAttribute("station", new Station());
        return "add_station";
    }

    @PostMapping(AuthenticationConstants.ADD_STATION_ENDPOINT)
    public String addStation(@ModelAttribute Station station) throws StationAlreadyExistsException {
        if (stationManager.findByCityAndAndStreetAndAndPostalCode(
                station.getCity(), station.getStreet(), station.getPostalCode()).isEmpty()) {
            stationManager.save(station);
            return "add_station";
        } else {
            throw new StationAlreadyExistsException(station.getCity() + ", " + station.getStreet() + ", " + station.getPostalCode());
        }
    }
}
