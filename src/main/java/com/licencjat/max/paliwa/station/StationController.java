package com.licencjat.max.paliwa.station;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/stations")
public class StationController {

    private StationManager stationManager;

    @Autowired
    public StationController(StationManager stationManager) {
        this.stationManager = stationManager;
    }

    @GetMapping("/add")
    public String addStationGET(Model model) {
        model.addAttribute("station", new Station());
        return "add_station";
    }

    @PostMapping("/add")
    public String addStation(@ModelAttribute Station station, RedirectAttributes redirectAttributes) {
        stationManager.save(station);
        return "add_station";
    }

    @GetMapping("/{id}")
    public String getStation(@PathVariable Long id) {
        return "test";
    }
}
