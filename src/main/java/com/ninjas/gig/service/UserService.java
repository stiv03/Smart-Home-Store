package com.ninjas.gig.service;

import com.ninjas.gig.entity.UserType;
import com.ninjas.gig.entity.UserAccount;
import com.ninjas.gig.repository.UserRepository;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    public record UserRequest(String firstName, String middleName, String lastName, String email, String username, String password, UserType userType) {}

    public List<UserAccount> addUser(@RequestBody UserRequest userRequest) {
        UserAccount newUser = new UserAccount();
        newUser.setFirstName(userRequest.firstName);
        newUser.setMiddleName(userRequest.middleName);
        newUser.setLastName(userRequest.lastName);
        newUser.setEmail(userRequest.email);
        newUser.setUsername(userRequest.username);
        newUser.setPassword(userRequest.password);
        newUser.setUserType(userRequest.userType);
        repository.save(newUser);
        return repository.findAll();
    }

    public List<UserAccount> displayAll() {
        return repository.findAll();
    }

}
