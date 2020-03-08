package com.licencjat.max.paliwa.price;

import com.licencjat.max.paliwa.station.Station;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private Station station;
}
