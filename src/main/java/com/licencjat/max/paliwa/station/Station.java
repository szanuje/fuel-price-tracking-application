package com.licencjat.max.paliwa.station;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.licencjat.max.paliwa.price.Price;
import com.licencjat.max.paliwa.reports.Report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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
    private String postalCode;
    private String city;
    private String description;
    @Column(precision = 17, scale = 15)
    private BigDecimal lat; //x
    @Column(precision = 18, scale = 15)
    private BigDecimal lon; //y

    @OneToMany(mappedBy = "station")
    @JsonManagedReference
    private List<Price> prices;

    @OneToMany(mappedBy = "station")
    @JsonManagedReference
    private List<Report> reports;
}
