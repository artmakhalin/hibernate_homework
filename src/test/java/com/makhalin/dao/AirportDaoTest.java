package com.makhalin.dao;

import com.makhalin.entity.Airport;
import com.makhalin.integration.IntegrationTestBase;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AirportDaoTest extends IntegrationTestBase {

    private final AirportDao airportDao = AirportDao.getInstance();

    @Test
    void findByCountryName() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var actualResult = airportDao.findByCountryName(session, "USA");
        assertThat(actualResult).hasSize(2);

        var cities = actualResult.stream()
                                 .map(Airport::getCity)
                                 .toList();
        assertThat(cities).containsExactlyInAnyOrder("New York", "Seattle");

        session.getTransaction().commit();
    }
}