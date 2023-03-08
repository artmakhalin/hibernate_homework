package com.makhalin.integration;

import com.makhalin.entity.Crew;
import com.makhalin.entity.Role;
import com.makhalin.util.HibernateTestUtil;
import lombok.Cleanup;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CrewIT {

    @Test
    void saveSuccess() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var crew = getAlex();

        assertThatNoException().isThrownBy(() -> session.save(crew));

        assertThatNoException().isThrownBy(() -> session.getTransaction()
                                                        .commit());
    }

    @Test
    void saveShouldThrowExceptionIfUniqueConstraintViolated() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var crew = getAlex();
        session.save(crew);

        session.getTransaction()
               .commit();

        var crew2 = getAlex();

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> session.save(crew2));
    }

    @Test
    void getSuccess() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var crew = getAlex();
        session.save(crew);

        session.getTransaction()
               .commit();

        var actualResult = session.get(Crew.class, crew.getId());

        assertThat(actualResult).isEqualTo(crew);
    }

    @Test
    void updateSuccess() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var crew = getAlex();
        session.save(crew);
        session.flush();
        crew.setFirstname("Peter");
        crew.setEmail("peter@test.com");
        crew.setMkkDate(LocalDate.of(2023, 10, 10));
        crew.setRole(Role.ADMIN);
        session.update(crew);

        session.getTransaction()
               .commit();

        var actualResult = session.get(Crew.class, crew.getId());

        assertAll(
                () -> assertThat(actualResult.getFirstname()).isEqualTo(crew.getFirstname()),
                () -> assertThat(actualResult.getEmail()).isEqualTo(crew.getEmail()),
                () -> assertThat(actualResult.getMkkDate()).isEqualTo(crew.getMkkDate()),
                () -> assertThat(actualResult.getRole()).isEqualTo(crew.getRole())
        );
    }

    @Test
    void deleteSuccess() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var crew = getAlex();
        session.save(crew);
        session.flush();
        session.delete(crew);

        session.getTransaction()
               .commit();

        var actualResult = session.get(Crew.class, crew.getId());

        assertThat(actualResult).isNull();
    }

    private Crew getAlex() {
        return Crew.builder()
                   .firstname("Alex")
                   .lastname("Test")
                   .email("alex@test.com")
                   .password("test")
                   .birthDate(LocalDate.of(1995, 1, 1))
                   .employmentDate(LocalDate.of(2015, 12, 5))
                   .role(Role.USER)
                   .build();
    }
}
