package com.licencjat.max.paliwa.station;

import com.licencjat.max.paliwa.price.Price;
import com.licencjat.max.paliwa.reports.Report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String street;
    private int postalCode;
    private String city;
    private BigDecimal lon; //x
    private BigDecimal lat; //y

    @OneToMany(mappedBy = "station")
    private List<Price> prices;

    @OneToMany(mappedBy = "station")
    private List<Report> reports;
}
