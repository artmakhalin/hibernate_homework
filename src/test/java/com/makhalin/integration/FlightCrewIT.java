package com.makhalin.integration;

import com.makhalin.entity.ClassOfService;
import com.makhalin.entity.Flight;
import com.makhalin.entity.FlightCrew;
import lombok.Cleanup;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThat;

class FlightCrewIT extends IntegrationTestBase {

    private Flight flight;

    @BeforeEach
    void saveFlight() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        flight = getFlight();
        session.save(flight);

        session.getTransaction()
               .commit();
    }

    @Test
    void saveSuccess() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var flightCrew = getFlightCrew();
        flightCrew.setFlight(flight);

        assertThatNoException().isThrownBy(() -> session.save(flightCrew));

        assertThatNoException().isThrownBy(() -> session.getTransaction()
                                                        .commit());
    }

    @Test
    void saveShouldThrowExceptionClassOfServiceIsNull() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var flightCrew = getFlightCrew();
        flightCrew.setFlight(flight);
        flightCrew.setClassOfService(null);

        assertThatExceptionOfType(PropertyValueException.class).isThrownBy(() -> session.save(flightCrew));
    }

    @Test
    void getSuccess() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var flightCrew = getFlightCrew();
        flightCrew.setFlight(flight);
        session.save(flightCrew);

        session.getTransaction()
               .commit();

        var actualResult = session.get(FlightCrew.class, flightCrew.getId());

        assertThat(actualResult).isEqualTo(flightCrew);
    }

    @Test
    void updateSuccess() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var flightCrew = getFlightCrew();
        flightCrew.setFlight(flight);
        session.save(flightCrew);
        session.flush();
        flightCrew.setClassOfService(ClassOfService.ECONOMY);
        session.update(flightCrew);

        session.getTransaction()
               .commit();

        var actualResult = session.get(FlightCrew.class, flightCrew.getId());

        assertThat(actualResult.getClassOfService()).isEqualTo(flightCrew.getClassOfService());
    }

    @Test
    void deleteSuccess() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var flightCrew = getFlightCrew();
        flightCrew.setFlight(flight);
        session.save(flightCrew);
        session.flush();
        session.delete(flightCrew);

        session.getTransaction()
               .commit();

        var actualResult = session.get(FlightCrew.class, flightCrew.getId());

        assertThat(actualResult).isNull();
    }
}
