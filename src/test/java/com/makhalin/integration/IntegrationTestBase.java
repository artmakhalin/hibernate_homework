package com.makhalin.integration;

import com.makhalin.entity.*;
import com.makhalin.util.HibernateTestUtil;
import lombok.Cleanup;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

public abstract class IntegrationTestBase {

    private Aircraft aircraft;
    private Country country;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private Crew crew;
    protected final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

    @BeforeEach
    void prepareDatabase() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        aircraft = getBoeing();
        session.save(aircraft);
        country = getUsa();
        session.save(country);
        departureAirport = getJfk();
        session.save(departureAirport);
        arrivalAirport = getSeattle();
        session.save(arrivalAirport);
        crew = getAlex();
        session.save(crew);

        session.getTransaction()
               .commit();
    }

    @AfterEach
    void closeSessionFactory() {
        sessionFactory.close();
    }

    protected Flight getFlight() {
        return Flight.builder()
                     .flightNo("D520")
                     .departureAirport(departureAirport)
                     .arrivalAirport(arrivalAirport)
                     .departureDate(LocalDate.of(2023, 3, 3))
                     .aircraft(aircraft)
                     .time(21600L)
                     .isTurnaround(false)
                     .isPassenger(false)
                     .build();
    }

    protected CrewAircraft getCrewAircraft() {
        return CrewAircraft.builder()
                           .permitDate(LocalDate.of(2020, 1, 1))
                           .crew(crew)
                           .aircraft(aircraft)
                           .build();
    }

    protected FlightCrew getFlightCrew() {
        return FlightCrew.builder()
                         .classOfService(ClassOfService.BUSINESS)
                         .crew(crew)
                         .build();
    }

    private Aircraft getBoeing() {
        return Aircraft.builder()
                       .model("Boeing-737")
                       .build();
    }

    private Country getUsa() {
        return Country.builder()
                      .name("USA")
                      .build();
    }

    private Airport getJfk() {
        return Airport.builder()
                      .code("JFK")
                      .city("New York")
                      .country(country)
                      .build();
    }

    private Airport getSeattle() {
        return Airport.builder()
                      .code("SEA")
                      .city("Seattle")
                      .country(country)
                      .build();
    }

    private Crew getAlex() {
        return Crew.builder()
                   .firstname("Alex")
                   .lastname("Test")
                   .email("alex@test.com")
                   .password("test")
                   .birthDate(LocalDate.of(1995, 1, 1))
                   .employmentDate(LocalDate.of(2015, 12, 5))
                   .role(Role.USER)
                   .build();
    }
}
