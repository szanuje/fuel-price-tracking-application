package com.licencjat.max.paliwa.price;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.licencjat.max.paliwa.station.Station;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String timestamp;
    private BigDecimal pb95;
    private BigDecimal pb98;
    private BigDecimal lpg;
    private BigDecimal diesel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    @ToString.Exclude
    @JsonBackReference
    private Station station;
}
