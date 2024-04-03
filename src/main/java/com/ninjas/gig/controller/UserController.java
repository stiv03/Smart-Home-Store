package com.ninjas.gig.controller;

import com.ninjas.gig.entity.UserAccount;
import com.ninjas.gig.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
