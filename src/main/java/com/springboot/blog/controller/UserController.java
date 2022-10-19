package com.springboot.blog.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.blog.payload.SignUpDto;
import com.springboot.blog.payload.UserDto;
import com.springboot.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2/auth")
@Slf4j
@Valid

public class UserController {

    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> saveUser(@RequestBody SignUpDto signUpDto){
        log.info("inside save User --> \n {}", signUpDto.toString());
        signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        return new ResponseEntity<>(userService.createUser(signUpDto), HttpStatus.CREATED);
    }

    @PostMapping(value="/change-password", consumes={"*/*"})
    public ResponseEntity<String> changePassword(@RequestBody MultiValueMap<String,String> params){
        log.info("inside change Password --> \n {}", params);
//        signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        return userService.changePassword(params);
    }


}
