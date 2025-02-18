package com.lms.onlinelms.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {


    private String accessToken;
    private String refreshToken;
    private String status;
    private String message;

}
