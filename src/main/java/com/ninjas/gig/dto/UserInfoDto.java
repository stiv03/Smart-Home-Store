package com.ninjas.gig.dto;

import com.ninjas.gig.entity.UserType;
import lombok.Data;

@Data
public class UserInfoDto {
    private Long id;
    private String photo;
    private String name;
    private String email;
    private String username;
    private UserType userType;
}
