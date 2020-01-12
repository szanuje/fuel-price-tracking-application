package com.licencjat.max.paliwa.price;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Price {

    private Long id;
    private int station_id;
    private String timestamp;
    private BigDecimal pb95;
    private BigDecimal pb98;
    private BigDecimal lpg;
    private BigDecimal diesel;
}
