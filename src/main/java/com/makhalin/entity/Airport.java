package com.makhalin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(of = "code")
@ToString(exclude = "country")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "airport")
public class Airport {

    @Id
    private String code;

    @Column(unique = true, nullable = false)
    private String city;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Country country;

    @Builder.Default
    @OneToMany(mappedBy = "departureAirport")
    private List<Flight> departureFlights = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "arrivalAirport")
    private List<Flight> arrivalFlights = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "transitAirport")
    private List<Flight> transitFlights = new ArrayList<>();
}
