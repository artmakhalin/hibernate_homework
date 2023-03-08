package com.makhalin.integration;

import com.makhalin.entity.Country;
import com.makhalin.util.HibernateTestUtil;
import lombok.Cleanup;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThat;

class CountryIT {

    @Test
    void saveSuccess() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var country = getUsa();

        assertThatNoException().isThrownBy(() -> session.save(country));

        assertThatNoException().isThrownBy(() -> session.getTransaction()
                                                        .commit());
    }

    @Test
    void saveShouldThrowExceptionIfUniqueConstraintViolated() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var country = getUsa();
        session.save(country);

        session.getTransaction()
               .commit();

        var country2 = getUsa();

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> session.save(country2));
    }

    @Test
    void getSuccess() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var country = getUsa();
        session.save(country);

        session.getTransaction()
               .commit();

        var actualResult = session.get(Country.class, country.getId());

        assertThat(actualResult).isEqualTo(country);
    }

    @Test
    void updateSuccess() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var country = getUsa();
        session.save(country);
        session.flush();
        country.setName("France");
        session.update(country);

        session.getTransaction()
               .commit();

        var actualResult = session.get(Country.class, country.getId());

        assertThat(actualResult.getName()).isEqualTo(country.getName());
    }

    @Test
    void deleteSuccess() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var country = getUsa();
        session.save(country);
        session.flush();
        session.delete(country);

        session.getTransaction()
               .commit();

        var actualResult = session.get(Country.class, country.getId());

        assertThat(actualResult).isNull();
    }

    private Country getUsa() {
        return Country.builder()
                      .name("USA")
                      .build();
    }
}
