package com.makhalin.integration;

import com.makhalin.entity.Airport;
import com.makhalin.entity.Country;
import com.makhalin.util.HibernateTestUtil;
import org.hibernate.PropertyValueException;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThat;

class AirportIT {

    private Country country;
    private final Session session = HibernateTestUtil.buildSessionFactory()
                                                     .openSession();

    @BeforeEach
    void prepareDatabase() {
        session.beginTransaction();

        country = getUsa();
        session.save(country);

        session.getTransaction()
               .commit();
    }

    @AfterEach
    void closeSessionFactory() {
        session.getSessionFactory()
               .close();
    }

    @Test
    void saveSuccess() {
        try (session) {
            session.beginTransaction();

            var airport = getJfk();

            assertThatNoException().isThrownBy(() -> session.save(airport));

            assertThatNoException().isThrownBy(() -> session.getTransaction()
                                                            .commit());
        }
    }

    @Test
    void saveShouldThrowExceptionIfCountryIsNull() {
        try (session) {
            session.beginTransaction();

            var airport = getJfk();
            airport.setCountry(null);

            assertThatExceptionOfType(PropertyValueException.class).isThrownBy(() -> session.save(airport));
        }
    }

    @Test
    void getSuccess() {
        try (session) {
            session.beginTransaction();

            var airport = getJfk();
            session.save(airport);

            session.getTransaction()
                   .commit();

            var actualResult = session.get(Airport.class, airport.getCode());

            assertThat(actualResult).isEqualTo(airport);
        }
    }

    @Test
    void updateSuccess() {
        try (session) {
            session.beginTransaction();

            var airport = getJfk();
            session.save(airport);
            session.flush();
            airport.setCity("Miami");
            session.update(airport);

            session.getTransaction()
                   .commit();

            var actualResult = session.get(Airport.class, airport.getCode());

            assertThat(actualResult.getCity()).isEqualTo(airport.getCity());
        }
    }

    @Test
    void deleteSuccess() {
        try (session) {
            session.beginTransaction();

            var airport = getJfk();
            session.save(airport);
            session.flush();
            session.delete(airport);

            session.getTransaction()
                   .commit();

            var actualResult = session.get(Airport.class, airport.getCode());

            assertThat(actualResult).isNull();
        }
    }

    private Airport getJfk() {
        return Airport.builder()
                      .code("JFK")
                      .city("New York")
                      .country(country)
                      .build();
    }

    private Country getUsa() {
        return Country.builder()
                      .name("USA")
                      .build();
    }
}
