package com.makhalin.entity;

import com.makhalin.util.HibernateUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CountryTest {

    @Test
    void checkCountrySuccess() {
        var country = Country.builder()
                             .id(25)
                             .countryName("USA")
                             .build();

        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(country);
            var actualResult = session.get(Country.class, country.getId());

            assertThat(actualResult).isEqualTo(country);
            session.getTransaction()
                   .commit();
        }
    }
}