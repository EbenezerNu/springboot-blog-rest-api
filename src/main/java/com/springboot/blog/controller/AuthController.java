package com.springboot.blog.controller;

import com.springboot.blog.entity.User;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.SignUpDto;
import com.springboot.blog.payload.UserDto;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<String> loginUser(@RequestBody LoginDto loginDto){
        log.info("Inside LoginUser -->");
        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User has successfully logged in!.", HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUserUp(@RequestBody SignUpDto signUpDto){

        log.info("Inside signUserUp -->");
        if(userRepository.existsByUsername(signUpDto.getUsername()))
            return new ResponseEntity<>("Username already exists!", HttpStatus.BAD_REQUEST);
        if(userRepository.existsByEmail(signUpDto.getEmail()))
            return new ResponseEntity<>("Email already exist!", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(userService.createUser(signUpDto), HttpStatus.CREATED);
    }

}
