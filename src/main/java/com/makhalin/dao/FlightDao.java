package com.makhalin.dao;

import com.makhalin.dto.FlightsFilter;
import com.makhalin.entity.*;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import java.time.LocalDate;
import java.util.List;

import static com.makhalin.entity.QCrew.crew;
import static com.makhalin.entity.QFlight.flight;
import static com.makhalin.entity.QFlightCrew.flightCrew;
import static org.hibernate.graph.GraphSemantic.LOAD;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlightDao {

    private static final FlightDao INSTANCE = new FlightDao();

    /*Находит все рейсы указанного БП за конкретный месяц*/

    public List<Flight> findByCrewAndMonth(Session session, FlightsFilter filter) {
        var flightGraph = session.createEntityGraph(Flight.class);
        flightGraph.addAttributeNodes("flightCrews", "time", "departureDate");
        var flightCrewsSubgraph = flightGraph.addSubgraph("flightCrews", FlightCrew.class);
        flightCrewsSubgraph.addAttributeNode("crew");

        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Flight.class);
        var flight = criteria.from(Flight.class);
        var flightCrews = flight.join(Flight_.flightCrews);
        var crew = flightCrews.join(FlightCrew_.crew);

        var predicates = CriteriaPredicate.builder()
                                          .add(filter.getCrewEmail(), email -> cb.equal(crew.get(Crew_.email), email))
                                          .add(LocalDate.of(filter.getYear(), filter.getMonth(), 1),
                                                  startMonth -> cb.between(
                                                          flight.get(Flight_.departureDate),
                                                          startMonth,
                                                          startMonth.plusDays(startMonth.lengthOfMonth())
                                                  ))
                                          .getPredicateArray();

        criteria.select(flight)
                .where(predicates);

        return session.createQuery(criteria)
                      .setHint(LOAD.getJpaHintName(), flightGraph)
                      .list();
    }

    /*Выводит суммарное время налета за месяц и месяц указанного БП за конкретный год*/

    List<Tuple> findSumFlightTimeByCrewAndYear(Session session, FlightsFilter filter) {
        var predicate = QPredicate.builder()
                                  .add(filter.getCrewEmail(), crew.email::eq)
                                  .add(filter.getYear(),
                                          year -> flight.departureDate.between(
                                                  LocalDate.of(year, 1, 1),
                                                  LocalDate.of(year, 12, 31)
                                          )
                                  )
                                  .buildAnd();

        return new JPAQuery<Tuple>(session)
                .select(flight.time.sum(), flight.departureDate.month())
                .from(flight)
                .join(flight.flightCrews, flightCrew)
                .join(flightCrew.crew, crew)
                .where(predicate)
                .groupBy(flight.departureDate.month())
                .orderBy(flight.departureDate.month()
                                             .asc())
                .fetch();
    }

    public static FlightDao getInstance() {
        return INSTANCE;
    }
}
