package com.springboot.blog.service.impl;

import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogAPIException;
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
        User editedUser = validateUser(params);
        editedUser.setPassword(passwordEncoder.encode(params.getFirst("new password")));
        userRepository.save(editedUser);

        return new ResponseEntity("User account password has been changed successfully!!", HttpStatus.valueOf(201));
    }

    public User validateUser(MultiValueMap<String, String> params) {

        String usernameOrEmail = "", oldPassword = "", newPassword = "";
        if(params.getFirst("username") != null && !params.getFirst("username").equals("")){
            usernameOrEmail = params.getFirst("username");
        }else if(params.getFirst("email") != null && !params.getFirst("email").equals("")){
            usernameOrEmail = params.getFirst("email");
        }else{
            throw new BlogAPIException("User account id not found; please provide user's username or email", HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = userRepository.findByUsernameOrEmail(usernameOrEmail);

        if(user.isEmpty()){
            throw new BlogAPIException("User account does not exist", HttpStatus.BAD_REQUEST);
        }

        if(params.getFirst("old password") != null && !params.getFirst("old password").equals("")){
            oldPassword = params.getFirst("old password");
        }else if(params.getFirst("password") != null && !params.getFirst("password").equals("")){
            oldPassword = params.getFirst("password");
        }else{
            throw new BlogAPIException("User account password not provided!!", HttpStatus.BAD_REQUEST);
        }

        if(!user.get().getPassword().equals(passwordEncoder.encode(oldPassword))){
            throw new BlogAPIException("Invalid user account password!!", HttpStatus.BAD_REQUEST);
        }

        if(params.getFirst("new password") == null || params.getFirst("new password").equals("") || params.getFirst("new password").length() < 8){
            throw new BlogAPIException("User account new password was not provided, or does not meet required standard; provide new password or length at least 8", HttpStatus.BAD_REQUEST);
        }

        return user.get();
    }



}
