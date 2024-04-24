package com.ninjas.gig.service;

import com.ninjas.gig.dto.UserInfoDto;
import com.ninjas.gig.entity.UserAccount;
import com.ninjas.gig.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // методи за OrderService
    public UserAccount getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }

    // клиент
    public UserInfoDto getUserDetails(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    UserInfoDto userDTO = new UserInfoDto();
                    userDTO.setId(user.getId());
                    userDTO.setPhoto(user.getPhoto());
                    userDTO.setName(user.getName());
                    userDTO.setEmail(user.getEmail());
                    userDTO.setUsername(user.getUsername());
                    userDTO.setUserType(user.getUserType());
                    return userDTO;
                })
                .orElse(null);

    }

    public void changeUserPhoto(Long userId, String newPhotoUrl) {
        Optional<UserAccount> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(user -> {
            user.setPhoto(newPhotoUrl);
            userRepository.save(user);
        });
    }


    // служител
    public List<UserAccount> getAllUsers() {
        return userRepository.findAll();
    }


    // админ

}
