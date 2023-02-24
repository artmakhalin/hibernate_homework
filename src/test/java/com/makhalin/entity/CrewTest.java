package com.makhalin.entity;

import com.makhalin.util.HibernateUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CrewTest {

    @Test
    void checkCrewSuccess() {
        var crew = Crew.builder()
                       .firstname("Test")
                       .lastname("Test")
                       .email("test@gmail.com")
                       .password("123")
                       .birthDate(LocalDate.of(1991, 5, 8))
                       .employmentDate(LocalDate.of(2020, 1, 1))
                       .mkkDate(LocalDate.of(2023, 10, 1))
                       .role(Role.USER)
                       .build();

        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(crew);
            var actualResult = session.get(Crew.class, crew.getId());

            assertThat(actualResult).isEqualTo(crew);
            session.getTransaction()
                   .commit();
        }
    }
}