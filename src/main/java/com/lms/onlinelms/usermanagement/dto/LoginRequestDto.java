package com.lms.onlinelms.usermanagement.dto;

import com.lms.onlinelms.usermanagement.validation.customAnnotations.Password;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @Email
    private String email;

    private String password;
}
