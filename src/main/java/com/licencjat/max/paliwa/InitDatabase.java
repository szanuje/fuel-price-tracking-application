package com.licencjat.max.paliwa;

import com.licencjat.max.paliwa.price.Price;
import com.licencjat.max.paliwa.price.PriceManager;
import com.licencjat.max.paliwa.reports.Report;
import com.licencjat.max.paliwa.reports.ReportManager;
import com.licencjat.max.paliwa.security.AuthenticationConstants;
import com.licencjat.max.paliwa.security.user.User;
import com.licencjat.max.paliwa.security.user.UserManager;
import com.licencjat.max.paliwa.station.Station;
import com.licencjat.max.paliwa.station.StationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

@Service
public class InitDatabase implements CommandLineRunner {

    private StationManager stationManager;
    private UserManager userManager;
    private PriceManager priceManager;
    private ReportManager reportManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public InitDatabase(StationManager stationManager,
                        UserManager userManager,
                        PasswordEncoder passwordEncoder,
                        PriceManager priceManager,
                        ReportManager reportManager
    ) {
        this.stationManager = stationManager;
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
        this.priceManager = priceManager;
        this.reportManager = reportManager;
    }

    @Override
    public void run(String... args) {

        priceManager.deleteAll();
        stationManager.deleteAll();
        userManager.deleteAll();
        reportManager.deleteAll();

        Station station1 = setStation("Orlen", "Kapelanka 2", 30316, "Kraków",
                new BigDecimal("31.23232"), new BigDecimal("22.32312312"));
        Station station2 = setStation("BP", "Ulica 2", 30316, "Kraków",
                new BigDecimal("31.23232"), new BigDecimal("22.32312312"));
        Station station3 = setStation("Lotos", "Ulica 3", 30316, "Kraków",
                new BigDecimal("31.23232"), new BigDecimal("22.32312312"));

        stationManager.saveAll(Arrays.asList(station1, station2, station3));

        Price price1 = setPrice(new Timestamp(new Date().getTime()).toString(), new BigDecimal("5.14"), new BigDecimal("5.45"),
                new BigDecimal("2.43"), new BigDecimal("4.88"), station1);
        Price price2 = setPrice(new Timestamp(new Date().getTime()).toString(), new BigDecimal("5.14"), new BigDecimal("5.45"),
                new BigDecimal("2.43"), new BigDecimal("4.88"), station2);
        Price price3 = setPrice(new Timestamp(new Date().getTime()).toString(), new BigDecimal("5.14"), new BigDecimal("5.45"),
                new BigDecimal("2.43"), new BigDecimal("4.88"), station3);

        priceManager.saveAll(Arrays.asList(price1, price2, price3));

        User user1 = setUser("admin", "admin@email.com", passwordEncoder.encode("admin"),
                "Name", "Surname", "ADMIN", AuthenticationConstants.adminRoleAuthoritiesAsString());
        User user2 = setUser("user", "user@email.com", passwordEncoder.encode("user"),
                "Name", "Surname", "USER", AuthenticationConstants.userRoleAuthoritiesAsString());

        userManager.saveAll(Arrays.asList(user1, user2));

        Report report1 = setReport(new Timestamp(new Date().getTime()).toString(), "desc1", station1);

        reportManager.saveAll(Arrays.asList(report1));
    }

    private Station setStation(String name, String street, int postalCode, String city, BigDecimal lon, BigDecimal lat) {
        Station station = new Station();
        station.setName(name);
        station.setStreet(street);
        station.setPostalCode(postalCode);
        station.setCity(city);
        station.setLon(lon);
        station.setLat(lat);
        return station;
    }

    private Price setPrice(String timestamp, BigDecimal pb95, BigDecimal pb98, BigDecimal lpg, BigDecimal diesel, Station station) {
        Price price = new Price();
        price.setTimestamp(timestamp);
        price.setPb95(pb95);
        price.setPb98(pb98);
        price.setLpg(lpg);
        price.setDiesel(diesel);
        price.setStation(station);
        return price;
    }

    private User setUser(String username, String email, String password, String name, String surname, String roles, String permissions) {
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

    private Report setReport(String datetime, String description, Station station) {
        Report report = new Report();
        report.setDatetime(datetime);
        report.setDescription(description);
        report.setActive(true);
        report.setStation(station);
        return report;
    }

}
