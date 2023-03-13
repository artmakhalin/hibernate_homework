package com.makhalin.dao;

import com.makhalin.dto.CrewFilter;
import com.makhalin.entity.Crew;
import com.makhalin.entity.CrewAircraft;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

import static com.makhalin.entity.QAircraft.aircraft;
import static com.makhalin.entity.QCrew.crew;
import static com.makhalin.entity.QCrewAircraft.crewAircraft;
import static org.hibernate.graph.GraphSemantic.LOAD;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CrewDao {

    private static final CrewDao INSTANCE = new CrewDao();

    /*Выводит всех БП, которые начали работать после указанной даты, с допуском на указанное ВС*/

    public List<Crew> findByAircraftAndEmploymentDate(Session session, CrewFilter filter) {
        var crewGraph = session.createEntityGraph(Crew.class);
        crewGraph.addAttributeNode("crewAircraft");
        var crewAircraftSubgraph = crewGraph.addSubGraph("crewAircraft", CrewAircraft.class);
        crewAircraftSubgraph.addAttributeNode("aircraft");

        var predicate = QPredicate.builder()
                                  .add(LocalDate.ofYearDay(filter.getStartYear(), 1), crew.employmentDate::after)
                                  .add(filter.getAircraftModel(), crewAircraft.aircraft.model::eq)
                                  .buildAnd();

        return new JPAQuery<Crew>(session)
                .select(crew)
                .from(crew)
                .join(crew.crewAircraft, crewAircraft)
                .join(crewAircraft.aircraft, aircraft)
                .where(predicate)
                .setHint(LOAD.getJpaHintName(), crewGraph)
                .fetch();
    }

    public static CrewDao getInstance() {
        return INSTANCE;
    }
}
