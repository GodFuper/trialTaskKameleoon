package com.example.trialtaskkameleoon.mappers;

import com.example.trialtaskkameleoon.dto.request.CreateUserRequest;
import com.example.trialtaskkameleoon.model.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {

    User toUser(CreateUserRequest createUserRequest);

}
