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
    public void createNewUser(@RequestBody @Valid CreateUserDto createUserDto){
        userService.createNewUser(createUserDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public BaseApi<?> loadAllUsers(){
        return BaseApi.builder()
                .isSuccess(true)
                .message("Active users loaded successfully!")
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .payload(userService.loadAllUsers())
                .build();
    }

}
