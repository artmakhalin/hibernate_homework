package com.makhalin.integration;

import com.makhalin.entity.CrewAircraft;
import lombok.Cleanup;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThat;

class CrewAircraftIT extends IntegrationTestBase {

    @Test
    void saveSuccess() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var crewAircraft = getAlexBoeing737();

        assertThatNoException().isThrownBy(() -> session.save(crewAircraft));

        assertThatNoException().isThrownBy(() -> session.getTransaction()
                                                        .commit());
    }

    @Test
    void saveShouldThrowExceptionIfPermitDateIsNull() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var crewAircraft = getAlexBoeing737();
        crewAircraft.setPermitDate(null);

        assertThatExceptionOfType(PropertyValueException.class).isThrownBy(() -> session.save(crewAircraft));
    }

    @Test
    void getSuccess() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var crewAircraft = getAlexBoeing737();
        session.save(crewAircraft);

        session.getTransaction()
               .commit();

        var actualResult = session.get(CrewAircraft.class, crewAircraft.getId());

        assertThat(actualResult).isEqualTo(crewAircraft);
    }

    @Test
    void updateSuccess() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var crewAircraft = getAlexBoeing737();
        session.save(crewAircraft);
        session.flush();
        crewAircraft.setPermitDate(LocalDate.of(2018, 5, 5));
        session.update(crewAircraft);

        session.getTransaction()
               .commit();

        var actualResult = session.get(CrewAircraft.class, crewAircraft.getId());

        assertThat(actualResult.getPermitDate()).isEqualTo(crewAircraft.getPermitDate());
    }

    @Test
    void deleteSuccess() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var crewAircraft = getAlexBoeing737();
        session.save(crewAircraft);
        session.flush();
        session.delete(crewAircraft);

        session.getTransaction()
               .commit();

        var actualResult = session.get(CrewAircraft.class, crewAircraft.getId());

        assertThat(actualResult).isNull();
    }
}
