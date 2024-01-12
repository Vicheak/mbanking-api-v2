package com.vicheak.mbankingapi.api.auth;

import com.vicheak.mbankingapi.api.auth.web.RegisterDto;
import com.vicheak.mbankingapi.api.user.web.CreateUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    CreateUserDto fromRegisterDtoToCreateUserDto(RegisterDto registerDto);

}
