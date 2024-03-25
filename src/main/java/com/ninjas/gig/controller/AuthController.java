package com.ninjas.gig.controller;

import com.ninjas.gig.dto.UserLoginDTO;
import com.ninjas.gig.dto.UserRegisterDTO;
import com.ninjas.gig.model.UserAccount;
import com.ninjas.gig.model.UserType;
import com.ninjas.gig.repository.UserRepository;
import com.ninjas.gig.service.AuthService;
import com.ninjas.gig.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDTO registerDTO){
        return authService.registerUser(registerDTO);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO loginDTO){
        return authService.loginUser(loginDTO);
    }

}
