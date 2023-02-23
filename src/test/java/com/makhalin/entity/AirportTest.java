package com.makhalin.entity;

import com.makhalin.util.HibernateUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AirportTest {

    @Test
    void checkAirportSuccess() {
        var airport = Airport.builder()
                             .code("JFK")
                             .city("New York")
                             .countryId(25)
                             .build();

        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(airport);
            var actualResult = session.get(Airport.class, airport.getCode());

            assertThat(actualResult).isEqualTo(airport);
            session.getTransaction()
                   .commit();
        }
    }
}