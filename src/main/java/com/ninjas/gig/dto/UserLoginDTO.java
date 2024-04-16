package com.ninjas.gig.dto;

import com.ninjas.gig.entity.UserType;
import lombok.Data;

@Data
public class UserLoginDTO {
    private String username;
    private String password;
    private UserType userType;
}
