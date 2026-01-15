package com.remake.poki.mapper;

import com.remake.poki.dto.UserDTO;
import com.remake.poki.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<UserDTO, User> {

    @Mapping(target = "level", source = "lever")
    @Mapping(target = "username", source = "user")
    UserDTO toDto(User entity);
}
