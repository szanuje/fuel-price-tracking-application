package com.licencjat.max.fuel;

import com.licencjat.max.fuel.price.Price;
import com.licencjat.max.fuel.price.PriceManager;
import com.licencjat.max.fuel.security.AuthenticationConstants;
import com.licencjat.max.fuel.security.user.User;
import com.licencjat.max.fuel.security.user.UserManager;
import com.licencjat.max.fuel.station.Station;
import com.licencjat.max.fuel.station.StationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class InitDatabase implements CommandLineRunner {

    private StationManager stationManager;
    private UserManager userManager;
    private PriceManager priceManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public InitDatabase(
            StationManager stationManager,
            UserManager userManager,
            PasswordEncoder passwordEncoder,
            PriceManager priceManager
    ) {
        this.stationManager = stationManager;
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
        this.priceManager = priceManager;
    }

    @Override
    public void run(String... args) {

        priceManager.deleteAll();
        stationManager.deleteAll();
        userManager.deleteAll();

        List<User> users = List.of(
                generateUser("admin", "admin@email.com", passwordEncoder.encode("admin"),
                        "Name", "Surname", "ADMIN", AuthenticationConstants.adminRoleAuthoritiesAsString()),
                generateUser("user", "user@email.com", passwordEncoder.encode("user"),
                        "Name", "Surname", "USER", AuthenticationConstants.userRoleAuthoritiesAsString())
        );

        userManager.saveAll(users);

        List<Station> stations = List.of(
                generateStation("BP Kapelanka", "Kapelanka 2", "30-309", "Kraków",
                        new BigDecimal("50.04424240166993"), new BigDecimal("19.921731948852543")),
                generateStation("watkem", "Rudna Mała 869", "35-900", "Rudna Mała",
                        new BigDecimal("50.1157788"), new BigDecimal("21.980679")),
                generateStation("Lotos Optima", "Kapelanka 14", "30-347", "Kraków",
                        new BigDecimal("50.0401092"), new BigDecimal("19.9242978306511")),
                generateStation("Stacja paliw Krak-Tar", "Kapelanka 30", "30-001", "Kraków",
                        new BigDecimal("50.038179"), new BigDecimal("19.925203")),
                generateStation("Stacja Paliw Tesco", "Kapelanka 56", "30-347", "Kraków",
                        new BigDecimal("50.033927"), new BigDecimal("19.924538")),
                generateStation("Shell", "Marii Konopnickiej 78", "30-333", "Kraków",
                        new BigDecimal("50.040151"), new BigDecimal("19.937445")),
                generateStation("BP", "Wadowicka 4", "30-415", "Kraków",
                        new BigDecimal("50.034370"), new BigDecimal("19.940091")),
                generateStation("Stacja paliw Orlen", "Podgórska 32", "33-332", "Kraków",
                        new BigDecimal("50.051688"), new BigDecimal("19.953870")),
                generateStation("Stacja paliw Orlen", "Włóczków 3", "33-103", "Kraków",
                        new BigDecimal("50.055109"), new BigDecimal("19.925272")),
                generateStation("Stacja Paliw Petrodom - Kraków", "Cystersów 21", "31-553", "Kraków",
                        new BigDecimal("50.061824"), new BigDecimal("19.969415"))
        );

        stationManager.saveAll(stations);

        createPricesAndAttachToStations(stations);
    }

    private Station generateStation(String name, String street, String postalCode, String city, BigDecimal lat, BigDecimal lon) {
        Station station = new Station();
        station.setName(name);
        station.setStreet(street);
        station.setPostalCode(postalCode);
        station.setCity(city);
        station.setLon(lon);
        station.setLat(lat);
        return station;
    }

    private Price generatePrice(String timestamp, double pb95, double pb98, double lpg, double diesel, Station station) {
        Price price = new Price();
        price.setTimestamp(timestamp);
        price.setPb95(pb95);
        price.setPb98(pb98);
        price.setLpg(lpg);
        price.setDiesel(diesel);
        price.setStation(station);
        return price;
    }

    private Timestamp lastTimestamp = Timestamp.valueOf("2020-03-20 10:10:10.0");

    private Timestamp generateNextTimestamp() {
        int minHours = 18;
        int maxHours = 50;
        int randomHours = (int) (Math.random() * ((maxHours - minHours) + 1)) + minHours;
        long duration = randomHours * 3600 * 1000;
        lastTimestamp.setTime(lastTimestamp.getTime() + duration);
        return lastTimestamp;
    }

    private double generatePb95() {
        double min = 4.90;
        double max = 5.58;
        return Math.round((Math.random() * (max - min) + min) * 100.0) / 100.0;
    }

    private double generatePb98() {
        double min = 5.48;
        double max = 5.99;
        return Math.round((Math.random() * (max - min) + min) * 100.0) / 100.0;
    }

    private double generateLpg() {
        double min = 1.90;
        double max = 2.40;
        return Math.round((Math.random() * (max - min) + min) * 100.0) / 100.0;
    }

    private double generateDiesel() {
        double min = 5.18;
        double max = 5.75;
        return Math.round((Math.random() * (max - min) + min) * 100.0) / 100.0;
    }

    private void createPricesAndAttachToStations(List<Station> stationsList) {
        List<Price> prices = new ArrayList<>();
        double zero = 0.0;
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            for (Station s : stationsList) {
                double pb95 = random.nextBoolean() ? generatePb95() : zero;
                double pb98 = random.nextBoolean() ? generatePb98() : zero;
                double lpg = random.nextBoolean() ? generateLpg() : zero;
                double diesel = random.nextBoolean() ? generateDiesel() : zero;
                prices.add(generatePrice(generateNextTimestamp().toString(), pb95, pb98, lpg, diesel, s));
            }
        }
        priceManager.saveAll(prices);
    }

    private User generateUser(String username, String email, String password, String name, String surname, String roles, String permissions) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setRoles(roles);
        user.setAuthorities(permissions);
        return user;
    }

}
