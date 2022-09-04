package com.springboot.blog.controller;

import com.springboot.blog.payload.SignUpDto;
import com.springboot.blog.payload.UserDto;
import com.springboot.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Slf4j
@Valid
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody SignUpDto signUpDto){
        log.info("inside save User --> \n {}", signUpDto.toString());
        return new ResponseEntity<>(userService.createUser(signUpDto), HttpStatus.CREATED);
    }


}
