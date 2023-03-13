package com.makhalin.dao;

import com.makhalin.dto.CrewFilter;
import com.makhalin.entity.Crew;
import com.makhalin.integration.IntegrationTestBase;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CrewDaoTest extends IntegrationTestBase {

    private final CrewDao crewDao = CrewDao.getInstance();

    @Test
    void findByAircraftAndEmploymentDate() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(getAlexBoeing737());
        session.flush();

        var filter = CrewFilter.builder()
                               .startYear(2015)
                               .aircraftModel("Boeing-737")
                               .build();
        var actualResult = crewDao.findByAircraftAndEmploymentDate(session, filter);
        assertThat(actualResult).hasSize(2);

        var emails = actualResult.stream()
                                 .map(Crew::getEmail)
                                 .toList();
        assertThat(emails).containsExactlyInAnyOrder("jake@test.com", "alex@test.com");

        session.getTransaction()
               .commit();
    }
}