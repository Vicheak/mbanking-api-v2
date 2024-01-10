package com.vicheak.mbankingapi.api.user;

import com.vicheak.mbankingapi.api.user.web.CreateUserDto;
import com.vicheak.mbankingapi.api.user.web.UserDto;

import java.util.List;

public interface UserService {

    /**
     * This method is used to create new user resource into the system
     * @param createUserDto is the request from client
     */
    void createNewUser(CreateUserDto createUserDto);

    /**
     * This method is used to retrieve user resources with status active
     * @return List<UserDto>
     */
    List<UserDto> loadAllUsers();

}
