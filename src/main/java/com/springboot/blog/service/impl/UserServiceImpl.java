package com.springboot.blog.service.impl;

import com.springboot.blog.entity.User;
import com.springboot.blog.payload.SignUpDto;
import com.springboot.blog.payload.UserDto;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public UserDto createUser(SignUpDto signUpDto) {
        User user = mapper.map(signUpDto, User.class);
        User newUser = userRepository.save(user);
        return mapper.map(newUser, UserDto.class);
    }

    @Override
    public ResponseEntity changePassword(MultiValueMap<String, String> params) {
        String usernameOrEmail = "", oldPassword = "", newPassword = "";
        if(params.getFirst("username") != null && !params.getFirst("username").equals("")){
            usernameOrEmail = params.getFirst("username");
        }else if(params.getFirst("email") != null && !params.getFirst("email").equals("")){
            usernameOrEmail = params.getFirst("email");
        }else{
            return new ResponseEntity("User Account id not found; please provide user's username or email", HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = userRepository.findByUsernameOrEmail(usernameOrEmail);

        if(user.isEmpty()){
            return new ResponseEntity("User Account does not exist", HttpStatus.BAD_REQUEST);
        }

        return null;
    }


}
