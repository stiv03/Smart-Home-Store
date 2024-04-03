//package com.ninjas.gig.controller;
//
//import com.ninjas.gig.dto.UserLoginDTO;
//import com.ninjas.gig.dto.UserRegisterDTO;
//import com.ninjas.gig.service.AuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@CrossOrigin(origins = "http://localhost:5173")
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    AuthService authService;
//    @Autowired
//    public AuthController(AuthService authService) {
//        this.authService = authService;
//    }
//
//    @PostMapping("register")
//    public ResponseEntity<String> register(@RequestBody UserRegisterDTO registerDTO){
//        return authService.registerUser(registerDTO);
//    }
//
//    @PostMapping("login")
//    public ResponseEntity<String> login(@RequestBody UserLoginDTO loginDTO){
//        return authService.loginUser(loginDTO);
//    }
//
//}
