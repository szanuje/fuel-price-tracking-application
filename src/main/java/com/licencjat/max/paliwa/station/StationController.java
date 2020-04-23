package com.licencjat.max.paliwa.station;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@Controller
@RequestMapping("/stations")
public class StationController {

    private StationManager stationManager;

    @Autowired
    public StationController(StationManager stationManager) {
        this.stationManager = stationManager;
    }

//    @PostMapping("/add")
//    public String addStationPOST(@ModelAttribute("station") Station station, RedirectAttributes redirectAttributes) {
//        return null;
//    }

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

    @GetMapping("/all")
    public String getAllStations(Model model) {
        Iterable<Station> stations = stationManager.findAll();
        stations.forEach((station) -> System.out.println(station.toString()));
        model.addAttribute("stations", stations);
        return "stations";
    }


    @GetMapping("/{id}")
    public Station getStation(@PathVariable Long id) {
        return stationManager.findById(id).orElse(null);
    }
}
