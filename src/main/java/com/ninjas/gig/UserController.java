package com.ninjas.gig;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    public record UserRequest(String name,String email, String userName, String password, UserType userType) {}

    @PostMapping("/user")
    public ResponseEntity<?> addUser(@RequestBody UserRequest userRequest) {
        UserAccount newUser = new UserAccount();
        newUser.setName(userRequest.name);
        newUser.setEmail(userRequest.email);
        newUser.setUserName(userRequest.userName);
        newUser.setPassword(userRequest.password);
        newUser.setUserType(userRequest.userType);
        repository.save(newUser);
        return ResponseEntity.ok(repository.findAll());
    }
    @GetMapping("/user")
    public List<UserAccount> hello1() {
        return repository.findAll();
    }

}
