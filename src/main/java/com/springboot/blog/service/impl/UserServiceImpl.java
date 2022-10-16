package com.springboot.blog.service.impl;

import com.springboot.blog.entity.User;
import com.springboot.blog.payload.SignUpDto;
import com.springboot.blog.payload.UserDto;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper mapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    
    

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

        if(params.getFirst("old password") != null && !params.getFirst("old password").equals("")){
            oldPassword = params.getFirst("old password");
        }else if(params.getFirst("password") != null && !params.getFirst("password").equals("")){
            oldPassword = params.getFirst("password");
        }else{
            return new ResponseEntity("User Account Password not provided!!", HttpStatus.BAD_REQUEST);
        }
        
        if(!user.get().getPassword().equals((passwordEncoder.encode(oldPassword)))){
            return new ResponseEntity("Invalid User Account Password!!", HttpStatus.BAD_REQUEST);
        }

        return null;
    }


}
