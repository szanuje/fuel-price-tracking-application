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

        Station station1 = setStation("BP Kapelanka", "Kapelanka 2", "30-309", "Kraków", "no description",
                new BigDecimal("50.04424240166993"), new BigDecimal("19.921731948852543"));
        Station station2 = setStation("watkem", "Rudna Mała 869", "35-900", "Rudna Mała","no description",
                new BigDecimal("50.1157788"), new BigDecimal("21.980679"));
        Station station3 = setStation("Lotos Optima", "Kapelanka undefined", "30-347", "Kraków","no description",
                new BigDecimal("50.0401092"), new BigDecimal("19.9242978306511"));
        Station station4 = setStation("Stacja paliw Krak-Tar", "Kapelanka 30", "30-001", "Kraków", "no description",
                new BigDecimal("50.038179"), new BigDecimal("19.925203"));
        Station station5 = setStation("Stacja Paliw Tesco", "Kapelanka 56", "30-347", "Kraków", "no description",
                new BigDecimal("50.033927"), new BigDecimal("19.924538"));
        Station station6 = setStation("Shell", "Marii Konopnickiej 78", "30-333", "Kraków", "no description",
                new BigDecimal("50.040151"), new BigDecimal("19.937445"));
        Station station7 = setStation("BP", "Wadowicka 4", "30-415", "Kraków", "no description",
                new BigDecimal("50.034370"), new BigDecimal("19.940091"));
        Station station8 = setStation("Stacja paliw Orlen", "Podgórska 32", "33-332", "Kraków", "no description",
                new BigDecimal("50.051688"), new BigDecimal("19.953870"));
        Station station9 = setStation("Stacja paliw Orlen", "Włóczków 3", "33-103", "Kraków", "no description",
                new BigDecimal("50.055109"), new BigDecimal("19.925272"));
        Station station10 = setStation("Stacja Paliw Petrodom - Kraków", "Cystersów 21", "31-553", "Kraków", "no description",
                new BigDecimal("50.061824"), new BigDecimal("19.969415"));

        stationManager.saveAll(Arrays.asList(station1, station2, station3, station4, station5, station6, station7, station8, station9, station10));

        Price price1 = setPrice(new Timestamp(new Date().getTime()).toString(), new BigDecimal("5.14"), new BigDecimal("5.45"),
                new BigDecimal("2.43"), new BigDecimal("4.88"), station1);
        Price price2 = setPrice(new Timestamp(new Date().getTime()).toString(), new BigDecimal("5.16"), new BigDecimal("5.41"),
                new BigDecimal("2.46"), new BigDecimal("4.83"), station2);
        Price price3 = setPrice(new Timestamp(new Date().getTime()).toString(), new BigDecimal("5.11"), new BigDecimal("5.42"),
                new BigDecimal("2.41"), new BigDecimal("4.82"), station3);
        Price price4 = setPrice(new Timestamp(new Date().getTime()).toString(), new BigDecimal("5.11"), new BigDecimal("5.42"),
                new BigDecimal("2.49"), new BigDecimal("4.78"), station4);
        Price price5 = setPrice(new Timestamp(new Date().getTime()).toString(), new BigDecimal("5.24"), new BigDecimal("5.42"),
                new BigDecimal("2.13"), new BigDecimal("4.88"), station5);
        Price price6 = setPrice(new Timestamp(new Date().getTime()).toString(), new BigDecimal("5.19"), new BigDecimal("5.41"),
                new BigDecimal("2.65"), new BigDecimal("4.68"), station6);
        Price price7 = setPrice(new Timestamp(new Date().getTime()).toString(), new BigDecimal("5.12"), new BigDecimal("5.34"),
                new BigDecimal("2.44"), new BigDecimal("4.69"), station7);
        Price price8 = setPrice(new Timestamp(new Date().getTime()).toString(), new BigDecimal("5.34"), new BigDecimal("5.35"),
                new BigDecimal("2.22"), new BigDecimal("4.90"), station8);
        Price price9 = setPrice(new Timestamp(new Date().getTime()).toString(), new BigDecimal("5.32"), new BigDecimal("5.23"),
                new BigDecimal("2.12"), new BigDecimal("4.70"), station9);
        Price price10 = setPrice(new Timestamp(new Date().getTime()).toString(), new BigDecimal("5.14"), new BigDecimal("5.44"),
                new BigDecimal("2.21"), new BigDecimal("4.93"), station10);
        Price price11 = setPrice(new Timestamp(new Date().getTime()).toString(), new BigDecimal("5.14"), new BigDecimal("5.44"),
                new BigDecimal("2.11"), new BigDecimal("4.95"), station10);

        priceManager.saveAll(Arrays.asList(price1, price2, price3, price4, price5, price6, price7, price8, price9, price10, price11));

        User user1 = setUser("admin", "admin@email.com", passwordEncoder.encode("admin"),
                "Name", "Surname", "ADMIN", AuthenticationConstants.adminRoleAuthoritiesAsString());
        User user2 = setUser("user", "user@email.com", passwordEncoder.encode("user"),
                "Name", "Surname", "USER", AuthenticationConstants.userRoleAuthoritiesAsString());

        userManager.saveAll(Arrays.asList(user1, user2));

        Report report1 = setReport(new Timestamp(new Date().getTime()).toString(), "desc1", station1);

        reportManager.saveAll(Arrays.asList(report1));
    }

    private Station setStation(String name, String street, String postalCode, String city, String description, BigDecimal lat, BigDecimal lon) {
        Station station = new Station();
        station.setName(name);
        station.setStreet(street);
        station.setPostalCode(postalCode);
        station.setCity(city);
        station.setLon(lon);
        station.setLat(lat);
        station.setDescription(description);
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
