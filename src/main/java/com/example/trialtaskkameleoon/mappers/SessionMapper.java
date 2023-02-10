package com.example.trialtaskkameleoon.mappers;

import com.example.trialtaskkameleoon.dto.response.SessionDto;
import com.example.trialtaskkameleoon.model.Session;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface SessionMapper {

    SessionDto toSessionDto(Session session);

}
