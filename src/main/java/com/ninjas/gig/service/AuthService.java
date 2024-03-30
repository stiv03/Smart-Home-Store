//package com.ninjas.gig.service;
//
//import com.ninjas.gig.dto.UserLoginDTO;
//import com.ninjas.gig.dto.UserRegisterDTO;
//import com.ninjas.gig.entity.UserAccount;
//import com.ninjas.gig.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@Service
//public class AuthService {
//
//    UserRepository userRepository;
//    PasswordEncoder passwordEncoder;
//    AuthenticationManager authenticationManager;
//
//
//    @Autowired
//    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager){
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.authenticationManager = authenticationManager;
//    }
//
//    public ResponseEntity<String> registerUser(@RequestBody UserRegisterDTO registerDTO) {
//        if (userRepository.existsByUsername(registerDTO.getUsername())) {
//            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
//        }
//        UserAccount newUser = new UserAccount();
//        newUser.setName(registerDTO.getName());
//        newUser.setEmail(registerDTO.getEmail());
//        newUser.setUsername(registerDTO.getUsername());
//        newUser.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
//        newUser.setUserType(registerDTO.getUserType());
//        userRepository.save(newUser);
//        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
//    }
//
//    public ResponseEntity<String> loginUser(@RequestBody UserLoginDTO loginDTO){
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
//                        loginDTO.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        } catch (Exception e){
//            e.printStackTrace();
//            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>("User signed successfully", HttpStatus.OK);
//    }
//}
