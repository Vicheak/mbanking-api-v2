package com.vicheak.mbankingapi.api.user;

import com.vicheak.mbankingapi.api.authority.Role;
import com.vicheak.mbankingapi.api.authority.RoleRepository;
import com.vicheak.mbankingapi.api.user.web.CreateUserDto;
import com.vicheak.mbankingapi.api.user.web.UserDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional
    @Override
    public void createNewUser(CreateUserDto createUserDto) {
        //check if username already exists
        if (userRepository.existsByUsernameIgnoreCase(createUserDto.username()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Username conflicts resources in the system!");

        //check if email already exists
        if (userRepository.existsByEmailIgnoreCase(createUserDto.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email conflicts resources in the system!");

        //check if phone number already exists
        if (userRepository.existsByPhoneNumberIgnoreCase(createUserDto.phoneNumber()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Phone number conflicts resources in the system!");

        //check if one signal id already exists
        if (Objects.nonNull(createUserDto.oneSignalId()) &&
                userRepository.existsByOneSignalIdIgnoreCase(createUserDto.oneSignalId()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "One signal ID conflicts resources in the system!");

        //check if student card number already exists
        if (Objects.nonNull(createUserDto.studentCardNo()) &&
                userRepository.existsByStudentCardNoIgnoreCase(createUserDto.studentCardNo()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Student card number conflicts resources in the system!");

        //check if all roles are valid roles in the system
        boolean isMatched = createUserDto.roleIds().stream()
                .allMatch(roleRepository::existsById);

        if (!isMatched)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Role is not valid in the system!");

        User newUser = userMapper.fromCreateUserDtoToUser(createUserDto);
        newUser.setUuid(UUID.randomUUID().toString());
        newUser.setIsVerified(true);
        newUser.setIsDeleted(false);

        userRepository.save(newUser);

        //set up user roles
        List<UserRole> userRoles = new ArrayList<>();

        createUserDto.roleIds().forEach(roleId ->
                userRoles.add(UserRole.builder()
                        .user(newUser)
                        .role(Role.builder().id(roleId).build())
                        .build()));

        userRoleRepository.saveAll(userRoles);
    }

    @Override
    public List<UserDto> loadAllUsers() {
        return userMapper.fromUserToUserDto(userRepository.findAllByIsDeleted(false));
    }

}
