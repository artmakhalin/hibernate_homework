package com.makhalin.dao;

import com.makhalin.entity.Airport;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;

import static com.makhalin.entity.QAirport.airport;
import static com.makhalin.entity.QCountry.country;
import static org.hibernate.graph.GraphSemantic.LOAD;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AirportDao {

    private static final AirportDao INSTANCE = new AirportDao();

    /*Выводит все аэропорты указанной страны*/

    public List<Airport> findByCountryName(Session session, String countryName) {
        var airportGraph = session.createEntityGraph(Airport.class);
        airportGraph.addAttributeNode("country");

        return new JPAQuery<Airport>(session)
                .select(airport)
                .from(airport)
                .join(airport.country, country)
                .where(country.name.eq(countryName))
                .setHint(LOAD.getJpaHintName(), airportGraph)
                .fetch();
    }

    public static AirportDao getInstance() {
        return INSTANCE;
    }
}
