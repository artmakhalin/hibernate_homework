package com.makhalin.entity;

import com.makhalin.util.HibernateUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AircraftTest {

    @Test
    void checkAircraftSuccess() {
        var aircraft = Aircraft.builder()
                               .model("Boeing-737")
                               .build();

        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(aircraft);
            session.getTransaction()
                   .commit();

            var actualResult = session.get(Aircraft.class, aircraft.getId());

            assertThat(actualResult).isEqualTo(aircraft);
        }
    }

}