package com.makhalin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flight")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String flightNo;
    @Column(nullable = false)
    private String departureAirportCode;
    private String transitAirportCode;
    @Column(nullable = false)
    private String arrivalAirportCode;
    @Column(nullable = false)
    private LocalDate departureDate;
    private Integer aircraftId;
    @Column(nullable = false)
    private Long flightTime;
    @Column(nullable = false)
    private Boolean isTurnaround;
    @Column(nullable = false)
    private Boolean isPassenger;
}
