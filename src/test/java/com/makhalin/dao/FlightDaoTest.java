package com.makhalin.dao;

import com.makhalin.dto.FlightsFilter;
import com.makhalin.entity.Flight;
import com.makhalin.integration.IntegrationTestBase;
import lombok.Cleanup;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FlightDaoTest extends IntegrationTestBase {

    private final FlightDao flightDao = FlightDao.getInstance();

    @Test
    void findByCrewAndMonth() {
        @Cleanup var session = sessionFactory.openSession();
        saveJfkSea(session);
        session.beginTransaction();

        var filter = FlightsFilter.builder()
                                  .crewEmail("alex@test.com")
                                  .month(3)
                                  .year(2023)
                                  .build();
        var actualResult = flightDao.findByCrewAndMonth(session, filter);
        assertThat(actualResult).hasSize(3);

        var flightNos = actualResult.stream()
                                    .map(Flight::getFlightNo)
                                    .toList();
        assertThat(flightNos).containsExactlyInAnyOrder("D520", "SU100", "SU25");

        session.getTransaction()
               .commit();
    }

    @Test
    void findSumFlightTimeByCrewAndYear() {
        @Cleanup var session = sessionFactory.openSession();
        saveJfkSea(session);
        session.beginTransaction();

        var filter = FlightsFilter.builder()
                                  .crewEmail("alex@test.com")
                                  .year(2023)
                                  .build();
        var actualResult = flightDao.findSumFlightTimeByCrewAndYear(session, filter);
        assertThat(actualResult).hasSize(3);

        var monthTimes = actualResult.stream()
                                     .map(it -> it.get(0, Long.class))
                                     .toList();
        assertThat(monthTimes).containsExactly(16924L, 7920L, 60300L);

        var months = actualResult.stream()
                                 .map(it -> it.get(1, Integer.class))
                                 .toList();
        assertThat(months).containsExactly(1, 2, 3);


        session.getTransaction()
               .commit();
    }

    private void saveJfkSea(Session session) {
        session.beginTransaction();

        session.save(getAlexBoeing737());
        var jfkSea = getJfkSea();
        session.save(jfkSea);
        var jfkSeaAlex = getJfkSeaAlex();
        jfkSeaAlex.setFlight(jfkSea);
        session.save(jfkSeaAlex);

        session.getTransaction()
               .commit();
    }
}