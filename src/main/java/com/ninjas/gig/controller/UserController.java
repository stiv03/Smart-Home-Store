package com.ninjas.gig.controller;

import java.util.*;

import com.ninjas.gig.dto.UserInfoDto;
import com.ninjas.gig.entity.Product;
import com.ninjas.gig.service.UserService;
import com.ninjas.gig.entity.UserAccount;
import com.ninjas.gig.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    // клиент

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserInfoDto> getUserDetails(@PathVariable Long userId) {
        UserInfoDto userDTO = userService.getUserDetails(userId);
        if (userDTO != null) {
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER','EMPLOYEE','ADMIN')")
    @PutMapping("/users/change-photo/{userId}")
    public ResponseEntity<String> changeUserPhoto(@PathVariable Long userId, @RequestBody String newPhotoUrl) {
        String cleanPhotoUrl = newPhotoUrl.replaceAll("\"", "");
        userService.changeUserPhoto(userId, cleanPhotoUrl);
        return ResponseEntity.ok("User photo changed successfully.");
    }

    // служител
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<List<UserAccount>> displayAllUsers() {
        List<UserAccount> users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

}
