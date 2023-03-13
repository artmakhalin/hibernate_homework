package com.makhalin.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CrewFilter {

    Integer startYear;
    String aircraftModel;
}
