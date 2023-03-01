package com.makhalin.entity;

import com.makhalin.util.HibernateUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class FlightTest {

    @Test
    void checkFlightSuccess() {
        var flight = Flight.builder()
                           .flightNo("SU102")
                           .departureAirportCode("SVO")
                           .arrivalAirportCode("JFK")
                           .departureDate(LocalDate.of(2022, 2, 26))
                           .aircraftId(5)
                           .time(30650L)
                           .isTurnaround(false)
                           .isPassenger(false)
                           .build();

        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(flight);
            session.getTransaction()
                   .commit();

            var actualResult = session.get(Flight.class, flight.getId());

            assertThat(actualResult).isEqualTo(flight);
        }
    }
}