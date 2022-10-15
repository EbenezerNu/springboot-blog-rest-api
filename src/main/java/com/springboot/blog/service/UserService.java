package com.springboot.blog.service;

import com.springboot.blog.payload.SignUpDto;
import com.springboot.blog.payload.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public interface UserService {

    UserDto createUser (SignUpDto signUpDto);

    ResponseEntity changePassword (MultiValueMap<String, String> params);
}
