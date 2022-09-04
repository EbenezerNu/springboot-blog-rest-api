package com.springboot.blog.service;

import com.springboot.blog.payload.SignUpDto;
import com.springboot.blog.payload.UserDto;

public interface UserService {

    UserDto createUser (SignUpDto signUpDto);
}
