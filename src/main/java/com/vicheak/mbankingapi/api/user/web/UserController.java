package com.vicheak.mbankingapi.api.user.web;

import com.vicheak.mbankingapi.api.account.AccountService;
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
    private final AccountService accountService;

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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{uuid}/accounts")
    public BaseApi<?> loadUserAccountsByUuid(@PathVariable String uuid) {
        return BaseApi.builder()
                .isSuccess(true)
                .message("Accounts of user, " + userService.loadUserByUuid(uuid).username())
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .payload(accountService.loadUserAccountsByUuid(uuid))
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userUuid}/accounts/{accountUuid}")
    public BaseApi<?> loadUserAccountByUuid(@PathVariable String userUuid,
                                            @PathVariable String accountUuid) {
        return BaseApi.builder()
                .isSuccess(true)
                .message("Accounts of user, " + userService.loadUserByUuid(userUuid).username())
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .payload(accountService.loadUserAccountByUuid(userUuid, accountUuid))
                .build();
    }

}
