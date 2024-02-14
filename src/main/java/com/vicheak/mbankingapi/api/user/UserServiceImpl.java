package com.vicheak.mbankingapi.api.user;

import com.vicheak.mbankingapi.api.authority.Role;
import com.vicheak.mbankingapi.api.authority.RoleRepository;
import com.vicheak.mbankingapi.api.user.web.CreateUserDto;
import com.vicheak.mbankingapi.api.user.web.UpdateUserDto;
import com.vicheak.mbankingapi.api.user.web.UserDto;
import com.vicheak.mbankingapi.security.CustomUserDetails;
import com.vicheak.mbankingapi.security.authorityconfig.RoleAuth;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void createNewUser(CreateUserDto createUserDto) {
        //check user validation
        checkUserValidation(createUserDto);

        //check if all roles are valid roles in the system
        checkValidRoles(createUserDto.roleIds());

        //set up new user
        User newUser = setupUser(createUserDto);

        userRepository.save(newUser);

        //set up user roles
        setUpUserRoles(createUserDto.roleIds(), newUser);
    }

    @Override
    public List<UserDto> loadAllUsers() {
        return userMapper.fromUserToUserDto(userRepository.findAllByIsDeleted(false));
    }

    @Override
    public UserDto loadUserByUuid(String uuid) {
        return userMapper.fromUserToUserDto(userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                ));
    }

    @Transactional
    @Override
    public void updateUserByUuid(String uuid, UpdateUserDto updateUserDto) {
        //check security context
        if (!checkSecurityContextControl())
            if (!checkSecurityContextUpdate(uuid))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Permission denied!");

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        //check if username conflicts resources in the system
        if (!updateUserDto.username().equalsIgnoreCase(user.getUsername()))
            if (userRepository.existsByUsernameIgnoreCase(updateUserDto.username()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Username conflicts resources in the system!");

        //check security context
        if (checkSecurityContextControl()) {
            //check if all roles are valid roles in the system
            checkValidRoles(updateUserDto.roleIds());

            userMapper.fromUpdateUserDtoToUser(user, updateUserDto);

            userRepository.save(user);

            userRoleRepository.deleteAll(user.getUserRoles());

            //set up user roles
            setUpUserRoles(updateUserDto.roleIds(), user);
        } else {
            //allowed customer
            userMapper.fromUpdateUserDtoToUser(user, updateUserDto);
            userRepository.save(user);
        }
    }

    @Transactional
    @Override
    public void disableUserByUuid(String uuid) {
        //check security context
        checkSecurityContext();

        if (checkSecurityContextUpdate(uuid))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "You cannot proceed this action! Permission denied!");

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        user.setIsDeleted(true);

        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUserByUuid(String uuid) {
        if (checkSecurityContextUpdate(uuid))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "You cannot proceed this action! Permission denied!");

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        userRepository.delete(user);
    }

    public boolean checkSecurityContextControl() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().contains(RoleAuth.ADMIN.getRoleName()) ||
                auth.getAuthorities().contains(RoleAuth.MANAGER.getRoleName());
    }

    public boolean checkSecurityContextUpdate(String uuid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();

        return customUserDetails.getUser().getUuid().equals(uuid);
    }

    public void checkSecurityContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //allow admin and manager
        if (auth.getAuthorities().contains(RoleAuth.CUSTOMER.getRoleName()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "This process is unauthorized! Permission denied!");
    }

    public void checkValidRoles(Set<Integer> roleIds) {
        boolean isMatched = roleIds.stream()
                .allMatch(roleRepository::existsById);

        if (!isMatched)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Role is not valid in the system!");
    }

    public void setUpUserRoles(Set<Integer> roleIds, User user) {
        List<UserRole> userRoles = new ArrayList<>();

        roleIds.forEach(roleId ->
                userRoles.add(UserRole.builder()
                        .user(user)
                        .role(Role.builder().id(roleId).build())
                        .build()));

        userRoleRepository.saveAll(userRoles);
    }

    public void checkUserValidation(CreateUserDto createUserDto) {
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
    }

    public User setupUser(CreateUserDto createUserDto) {
        User newUser = userMapper.fromCreateUserDtoToUser(createUserDto);
        newUser.setUuid(UUID.randomUUID().toString());
        //encrypt user password
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setIsVerified(true);
        newUser.setIsDeleted(false);
        return newUser;
    }

}
