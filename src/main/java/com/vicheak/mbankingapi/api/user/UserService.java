package com.vicheak.mbankingapi.api.user;

import com.vicheak.mbankingapi.api.user.web.CreateUserDto;
import com.vicheak.mbankingapi.api.user.web.UpdateUserDto;
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

    /**
     * This method is used to retrieve user resource by uuid
     * @param uuid is the path parameter from client
     * @return UserDto
     */
    UserDto loadUserByUuid(String uuid);

    /**
     * This method is used to update existing user resource by uuid
     * @param uuid is the path parameter from client
     * @param updateUserDto is the request from client
     */
    void updateUserByUuid(String uuid, UpdateUserDto updateUserDto);

    /**
     * This method is used to disable existing user resource by uuid
     * @param uuid is the path parameter from client
     */
    void disableUserByUuid(String uuid);

    /**
     * This method is used to delete existing user resource by uuid permanently
     * @param uuid is the path parameter from client
     */
    void deleteUserByUuid(String uuid);

}
