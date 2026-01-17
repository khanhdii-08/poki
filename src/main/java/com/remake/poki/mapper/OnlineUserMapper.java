package com.remake.poki.mapper;

import com.remake.poki.dto.OnlineUserDTO;
import com.remake.poki.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OnlineUserMapper extends BaseMapper<OnlineUserDTO, User> {

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "username", source = "user")
    OnlineUserDTO toDto(User entity);
}
