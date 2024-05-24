package com.vicheak.mbankingapi.api.user.web;

import com.vicheak.mbankingapi.api.user.UserService;
import com.vicheak.mbankingapi.base.BaseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createNewUser(@RequestBody @Valid CreateUserDto createUserDto) {
        userService.createNewUser(createUserDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public BaseApi<?> loadAllUsers() {
        return BaseApi.builder()
                .isSuccess(true)
                .message("Active users loaded successfully!")
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .payload(userService.loadAllUsers())
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{uuid}")
    public BaseApi<?> loadUserByUuid(@PathVariable String uuid) {
        return BaseApi.builder()
                .isSuccess(true)
                .message("User loaded successfully!")
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .payload(userService.loadUserByUuid(uuid))
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}")
    public void updateUserByUuid(@PathVariable String uuid,
                                 @RequestBody @Valid UpdateUserDto updateUserDto) {
        userService.updateUserByUuid(uuid, updateUserDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}/disable")
    public void disableUserByUuid(@PathVariable String uuid) {
        userService.disableUserByUuid(uuid);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    public void deleteUserByUuid(@PathVariable String uuid) {
        userService.deleteUserByUuid(uuid);
    }

}
