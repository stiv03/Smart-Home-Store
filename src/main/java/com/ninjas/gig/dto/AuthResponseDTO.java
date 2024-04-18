package com.ninjas.gig.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer ";
    private UserInfoDto userInfoDto;

    public AuthResponseDTO(String accessToken, UserInfoDto userInfoDto){
        this.accessToken = accessToken;
        this.userInfoDto = userInfoDto;
    }
}
