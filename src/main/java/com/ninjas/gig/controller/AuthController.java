package com.ninjas.gig.controller;

import com.ninjas.gig.dto.UserLoginDTO;
import com.ninjas.gig.dto.UserRegisterDTO;
import com.ninjas.gig.model.UserAccount;
import com.ninjas.gig.repository.UserRepository;
import com.ninjas.gig.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDTO dto){
        UserAccount user = authService.registerUser(dto);
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO dto){
        return null;
    }


}
