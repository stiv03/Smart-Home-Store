package com.ninjas.gig.controller;

import java.util.*;

import com.ninjas.gig.entity.Product;
import com.ninjas.gig.service.UserService;
import com.ninjas.gig.entity.UserAccount;
import com.ninjas.gig.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<List<UserAccount>> displayAllUsers() {
        List<UserAccount> users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

}
