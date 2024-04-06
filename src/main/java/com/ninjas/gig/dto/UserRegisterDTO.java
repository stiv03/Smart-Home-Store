package com.ninjas.gig.dto;

import com.ninjas.gig.entity.UserType;
import lombok.Data;

@Data
public class UserRegisterDTO {
    private String name;
    private String email;
    private String username;
    private String password;
    private UserType userType;

}
