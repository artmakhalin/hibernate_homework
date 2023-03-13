package com.makhalin.integration;

import com.makhalin.entity.Flight;
import lombok.Cleanup;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FlightIT extends IntegrationTestBase {

    @Test
    void saveSuccess() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var flight = getJfkSea();

        assertThatNoException().isThrownBy(() -> session.save(flight));

        assertThatNoException().isThrownBy(() -> session.getTransaction()
                                                        .commit());
    }

    @Test
    void saveShouldThrowExceptionIfAircraftIsNull() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var flight = getJfkSea();
        flight.setAircraft(null);

        assertThatExceptionOfType(PropertyValueException.class).isThrownBy(() -> session.save(flight));
    }

    @Test
    void getSuccess() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var flight = getJfkSea();
        session.save(flight);

        session.getTransaction()
               .commit();

        var actualResult = session.get(Flight.class, flight.getId());

        assertThat(actualResult).isEqualTo(flight);
    }

    @Test
    void updateSuccess() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var flight = getJfkSea();
        session.save(flight);
        session.flush();
        flight.setFlightNo("X555");
        flight.setTime(3600L);
        flight.setDepartureDate(LocalDate.of(2020, 1, 1));
        flight.setIsTurnaround(true);
        session.update(flight);

        session.getTransaction()
               .commit();

        var actualResult = session.get(Flight.class, flight.getId());

        assertAll(
                () -> assertThat(actualResult.getFlightNo()).isEqualTo(flight.getFlightNo()),
                () -> assertThat(actualResult.getTime()).isEqualTo(flight.getTime()),
                () -> assertThat(actualResult.getDepartureDate()).isEqualTo(flight.getDepartureDate()),
                () -> assertThat(actualResult.getIsTurnaround()).isEqualTo(flight.getIsTurnaround())
        );
    }

    @Test
    void deleteSuccess() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var flight = getJfkSea();
        session.save(flight);
        session.flush();
        session.delete(flight);

        session.getTransaction()
               .commit();

        var actualResult = session.get(Flight.class, flight.getId());

        assertThat(actualResult).isNull();
    }
}
