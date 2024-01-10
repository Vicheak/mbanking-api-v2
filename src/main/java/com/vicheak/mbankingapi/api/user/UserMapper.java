package com.vicheak.mbankingapi.api.user;

import com.vicheak.mbankingapi.api.user.web.CreateUserDto;
import com.vicheak.mbankingapi.api.user.web.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromCreateUserDtoToUser(CreateUserDto createUserDto);

    UserDto fromUserToUserDto(User user) ;

    List<UserDto> fromUserToUserDto(List<User> users);

}
