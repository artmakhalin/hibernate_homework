package com.makhalin.integration;

import com.makhalin.entity.*;
import com.makhalin.util.HibernateTestUtil;
import lombok.Cleanup;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

public abstract class IntegrationTestBase {

    private Aircraft boeing737;
    private Aircraft boeing777;
    private Aircraft airbus320;
    private Country usa;
    private Country russia;
    private Country france;
    private Airport jfk;
    private Airport sea;
    private Airport svo;
    private Airport led;
    private Airport vog;
    private Airport cdg;
    private Crew alex;
    private Crew jake;
    private Crew bob;
    private Flight svoJfk;
    private Flight svoLed;
    private Flight svoVog;
    private Flight svoCdg;
    private Flight ledVog;
    protected final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

    @BeforeEach
    void prepareDatabase() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        boeing737 = getBoeing737();
        session.save(boeing737);
        boeing777 = getBoeing777();
        session.save(boeing777);
        airbus320 = getAirbus320();
        session.save(airbus320);

        usa = getUsa();
        session.save(usa);
        russia = getRussia();
        session.save(russia);
        france = getFrance();
        session.save(france);
        jfk = getJfk();
        session.save(jfk);
        sea = getSeattle();
        session.save(sea);
        svo = getSheremetyevo();
        session.save(svo);
        led = getPulkovo();
        session.save(led);
        vog = getVolgograd();
        session.save(vog);
        cdg = getParis();
        session.save(cdg);

        alex = getAlex();
        session.save(alex);
        session.save(getAlexBoeing777());
        session.save(getAlexAirbus320());
        bob = getBob();
        session.save(bob);
        session.save(getBobAirbus320());
        jake = getJake();
        session.save(jake);
        session.save(getJakeBoeing737());

        svoJfk = getSvoJfk();
        session.save(svoJfk);
        session.save(getSvoJfkAlex());
        svoLed = getSvoLed();
        session.save(svoLed);
        session.save(getSvoLedAlex());
        svoVog = getSvoVog();
        session.save(svoVog);
        session.save(getSvoVogBob());
        session.save(getSvoVogAlex());
        svoCdg = getSvoCdg();
        session.save(svoCdg);
        session.save(getSvoCdgAlex());
        ledVog = getLedVog();
        session.save(ledVog);
        session.save(getLedVogAlex());

        session.getTransaction()
               .commit();
    }

    @AfterEach
    void closeSessionFactory() {
        sessionFactory.close();
    }

    private Aircraft getBoeing737() {
        return Aircraft.builder()
                       .model("Boeing-737")
                       .build();
    }

    private Aircraft getBoeing777() {
        return Aircraft.builder()
                       .model("Boeing-777")
                       .build();
    }

    private Aircraft getAirbus320() {
        return Aircraft.builder()
                       .model("Airbus-320")
                       .build();
    }

    private Country getUsa() {
        return Country.builder()
                      .name("USA")
                      .build();
    }

    private Country getRussia() {
        return Country.builder()
                      .name("Russia")
                      .build();
    }

    private Country getFrance() {
        return Country.builder()
                      .name("France")
                      .build();
    }

    private Airport getJfk() {
        return Airport.builder()
                      .code("JFK")
                      .city("New York")
                      .country(usa)
                      .build();
    }

    private Airport getSheremetyevo() {
        return Airport.builder()
                      .code("SVO")
                      .city("Moscow")
                      .country(russia)
                      .build();
    }

    private Airport getPulkovo() {
        return Airport.builder()
                      .code("LED")
                      .city("St Petersburg")
                      .country(russia)
                      .build();
    }

    private Airport getVolgograd() {
        return Airport.builder()
                      .code("VOG")
                      .city("Volgograd")
                      .country(russia)
                      .build();
    }

    private Airport getSeattle() {
        return Airport.builder()
                      .code("SEA")
                      .city("Seattle")
                      .country(usa)
                      .build();
    }

    private Airport getParis() {
        return Airport.builder()
                      .code("CDG")
                      .city("Paris")
                      .country(france)
                      .build();
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

    protected CrewAircraft getAlexBoeing737() {
        return CrewAircraft.builder()
                           .permitDate(LocalDate.of(2020, 1, 1))
                           .crew(alex)
                           .aircraft(boeing737)
                           .build();
    }

    private CrewAircraft getAlexBoeing777() {
        return CrewAircraft.builder()
                           .permitDate(LocalDate.of(2021, 1, 1))
                           .crew(alex)
                           .aircraft(boeing777)
                           .build();
    }

    private CrewAircraft getAlexAirbus320() {
        return CrewAircraft.builder()
                           .permitDate(LocalDate.of(2021, 1, 1))
                           .crew(alex)
                           .aircraft(airbus320)
                           .build();
    }

    private Crew getJake() {
        return Crew.builder()
                   .firstname("Jake")
                   .lastname("Test")
                   .email("jake@test.com")
                   .password("test")
                   .birthDate(LocalDate.of(1995, 1, 1))
                   .employmentDate(LocalDate.of(2020, 12, 5))
                   .role(Role.USER)
                   .build();
    }

    private CrewAircraft getJakeBoeing737() {
        return CrewAircraft.builder()
                           .permitDate(LocalDate.of(2021, 1, 1))
                           .crew(jake)
                           .aircraft(boeing737)
                           .build();
    }

    private Crew getBob() {
        return Crew.builder()
                   .firstname("Bob")
                   .lastname("Test")
                   .email("bob@test.com")
                   .password("test")
                   .birthDate(LocalDate.of(1995, 1, 1))
                   .employmentDate(LocalDate.of(2014, 12, 5))
                   .role(Role.USER)
                   .build();
    }

    private CrewAircraft getBobAirbus320() {
        return CrewAircraft.builder()
                           .permitDate(LocalDate.of(2021, 1, 1))
                           .crew(bob)
                           .aircraft(airbus320)
                           .build();
    }

    protected Flight getJfkSea() {
        return Flight.builder()
                     .flightNo("D520")
                     .departureAirport(jfk)
                     .arrivalAirport(sea)
                     .departureDate(LocalDate.of(2023, 3, 3))
                     .aircraft(boeing737)
                     .time(21600L)
                     .isTurnaround(false)
                     .isPassenger(false)
                     .build();
    }

    protected FlightCrew getJfkSeaAlex() {
        return FlightCrew.builder()
                         .classOfService(ClassOfService.BUSINESS)
                         .crew(alex)
                         .build();
    }

    private Flight getSvoJfk() {
        return Flight.builder()
                     .flightNo("SU100")
                     .departureAirport(svo)
                     .arrivalAirport(jfk)
                     .departureDate(LocalDate.of(2023, 3, 10))
                     .aircraft(boeing777)
                     .time(30600L)
                     .isTurnaround(false)
                     .isPassenger(false)
                     .build();
    }

    private FlightCrew getSvoJfkAlex() {
        return FlightCrew.builder()
                         .classOfService(ClassOfService.BUSINESS)
                         .flight(svoJfk)
                         .crew(alex)
                         .build();
    }

    private Flight getSvoLed() {
        return Flight.builder()
                     .flightNo("SU25")
                     .departureAirport(svo)
                     .arrivalAirport(led)
                     .departureDate(LocalDate.of(2023, 3, 15))
                     .aircraft(airbus320)
                     .time(8100L)
                     .isTurnaround(true)
                     .isPassenger(false)
                     .build();
    }

    private FlightCrew getSvoLedAlex() {
        return FlightCrew.builder()
                         .classOfService(ClassOfService.BUSINESS)
                         .flight(svoLed)
                         .crew(alex)
                         .build();
    }

    private Flight getSvoVog() {
        return Flight.builder()
                     .flightNo("SU1188")
                     .departureAirport(svo)
                     .arrivalAirport(vog)
                     .departureDate(LocalDate.of(2023, 1, 18))
                     .aircraft(airbus320)
                     .time(5404L)
                     .isTurnaround(true)
                     .isPassenger(false)
                     .build();
    }

    private FlightCrew getSvoVogBob() {
        return FlightCrew.builder()
                         .classOfService(ClassOfService.ECONOMY)
                         .flight(svoVog)
                         .crew(bob)
                         .build();
    }

    private FlightCrew getSvoVogAlex() {
        return FlightCrew.builder()
                         .classOfService(ClassOfService.BUSINESS)
                         .flight(svoVog)
                         .crew(alex)
                         .build();
    }

    private Flight getSvoCdg() {
        return Flight.builder()
                     .flightNo("SU2454")
                     .departureAirport(svo)
                     .arrivalAirport(cdg)
                     .departureDate(LocalDate.of(2023, 1, 15))
                     .aircraft(airbus320)
                     .time(11520L)
                     .isTurnaround(true)
                     .isPassenger(false)
                     .build();
    }

    private FlightCrew getSvoCdgAlex() {
        return FlightCrew.builder()
                         .classOfService(ClassOfService.BUSINESS)
                         .flight(svoCdg)
                         .crew(alex)
                         .build();
    }

    private Flight getLedVog() {
        return Flight.builder()
                     .flightNo("DP612")
                     .departureAirport(led)
                     .arrivalAirport(vog)
                     .departureDate(LocalDate.of(2023, 2, 19))
                     .aircraft(airbus320)
                     .time(7920L)
                     .isTurnaround(true)
                     .isPassenger(false)
                     .build();
    }

    private FlightCrew getLedVogAlex() {
        return FlightCrew.builder()
                         .classOfService(ClassOfService.BUSINESS)
                         .flight(ledVog)
                         .crew(alex)
                         .build();
    }
}
