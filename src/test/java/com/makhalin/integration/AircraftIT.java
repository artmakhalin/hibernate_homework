package com.makhalin.integration;

import com.makhalin.entity.Aircraft;
import com.makhalin.util.HibernateTestUtil;
import lombok.Cleanup;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThat;


class AircraftIT {

    @Test
    void saveSuccess() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var aircraft = getBoeing();

        assertThatNoException().isThrownBy(() -> session.save(aircraft));

        assertThatNoException().isThrownBy(() -> session.getTransaction()
                                                        .commit());
    }

    @Test
    void saveShouldThrowExceptionIfUniqueConstraintViolated() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var aircraft = getBoeing();
        session.save(aircraft);

        session.getTransaction()
               .commit();

        var aircraft2 = getBoeing();

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> session.save(aircraft2));
    }

    @Test
    void getSuccess() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var aircraft = getBoeing();
        session.save(aircraft);

        session.getTransaction()
               .commit();

        var actualResult = session.get(Aircraft.class, aircraft.getId());

        assertThat(actualResult).isEqualTo(aircraft);
    }

    @Test
    void updateSuccess() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var aircraft = getBoeing();
        session.save(aircraft);
        session.flush();
        aircraft.setModel("Airbus-350");
        session.update(aircraft);

        session.getTransaction()
               .commit();

        var actualResult = session.get(Aircraft.class, aircraft.getId());

        assertThat(actualResult.getModel()).isEqualTo(aircraft.getModel());
    }

    @Test
    void deleteSuccess() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var aircraft = getBoeing();
        session.save(aircraft);
        session.flush();
        session.delete(aircraft);
        session.getTransaction()
               .commit();

        var actualResult = session.get(Aircraft.class, aircraft.getId());

        assertThat(actualResult).isNull();
    }

    private Aircraft getBoeing() {
        return Aircraft.builder()
                       .model("Boeing-737")
                       .build();
    }
}
