package com.licencjat.max.paliwa;

import com.licencjat.max.paliwa.security.user.User;
import com.licencjat.max.paliwa.security.user.UserManager;
import com.licencjat.max.paliwa.station.Station;
import com.licencjat.max.paliwa.station.StationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class InitDatabase implements CommandLineRunner {

    private StationManager stationManager;
    private UserManager userManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public InitDatabase(StationManager stationManager, UserManager userManager, PasswordEncoder passwordEncoder) {
        this.stationManager = stationManager;
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        stationManager.deleteAll();
        stationManager.save(new Station(1L, "Orlen", "Kapelanka 2", 30316, new BigDecimal("31.23232"), new BigDecimal("22.32312312")));
        stationManager.save(new Station(2L, "BP", "Ulica 2", 30316, new BigDecimal("31.23232"), new BigDecimal("22.32312312")));
        stationManager.save(new Station(3L, "Lotos", "Ulica 3", 30316, new BigDecimal("31.23232"), new BigDecimal("22.32312312")));

        userManager.deleteAll();
        userManager.save(new User(1L, "admin", "user_email", passwordEncoder.encode("admin"),
                "Name", "Surname", "ADMIN", "xd"));
        userManager.save(new User(2L, "user", "user_email", passwordEncoder.encode("pass"),
                "Name", "Surname", "USER", "xd"));

    }
}
