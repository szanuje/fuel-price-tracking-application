package com.licencjat.max.fuel.price;

import com.licencjat.max.fuel.exceptions.EmptyPricesException;
import com.licencjat.max.fuel.exceptions.StationNotFoundException;
import com.licencjat.max.fuel.security.AuthenticationConstants;
import com.licencjat.max.fuel.station.Station;
import com.licencjat.max.fuel.station.StationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.stream.Stream;

@RestController
@RequestMapping(AuthenticationConstants.ADD_PRICE_ENDPOINT)
public class PriceRestController {

    private PriceManager priceManager;
    private StationManager stationManager;

    @Autowired
    public PriceRestController(PriceManager priceManager, StationManager stationManager) {
        this.priceManager = priceManager;
        this.stationManager = stationManager;
    }

    @PostMapping
    public String getAllPrices(
            @RequestParam int id,
            @RequestParam(value = "pb95", defaultValue = "0") double pb95,
            @RequestParam(value = "pb98", defaultValue = "0") double pb98,
            @RequestParam(value = "lpg", defaultValue = "0") double lpg,
            @RequestParam(value = "diesel", defaultValue = "0") double diesel
    ) throws EmptyPricesException, StationNotFoundException {
        System.out.println("id = " + id + ", pb95 = " + pb95 + ", pb98 = " + pb98 + ", lpg = " + lpg + ", diesel = " + diesel);
        if (Stream.of(pb95, pb98, lpg, diesel).allMatch(x -> x == 0.0))
            throw new EmptyPricesException("All prices are blank");
        Station station = stationManager.findById((long) id).orElseThrow(() -> new StationNotFoundException(id));

        Price price = new Price();
        price.setTimestamp(new Timestamp(new Date().getTime()).toString());
        price.setPb95(pb95);
        price.setPb98(pb98);
        price.setLpg(lpg);
        price.setDiesel(diesel);
        price.setStation(station);
        priceManager.save(price);
        return "success";
    }
}
