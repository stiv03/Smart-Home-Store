package com.ninjas.gig.controller;

import java.util.*;

import com.ninjas.gig.service.UserService;
import com.ninjas.gig.entity.UserAccount;
import com.ninjas.gig.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class UserController {
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserService userService;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/user")
    public List<UserAccount> addUser(@RequestBody UserService.UserRequest userRequest) {
        return userService.addUser(userRequest);
    }

    @GetMapping("/user")
    public List<UserAccount> displayAllUsers() {
        return userService.displayAll();
    }


}
