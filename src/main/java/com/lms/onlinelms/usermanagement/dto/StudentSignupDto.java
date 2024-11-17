package com.lms.onlinelms.usermanagement.dto;

import com.lms.onlinelms.usermanagement.model.Image;
import com.lms.onlinelms.usermanagement.validation.customAnnotations.Password;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentSignupDto {

    @Size(min = 3, max = 15)
    @NotBlank
    private String firstName;
    @Size(min = 3, max = 15)
    @NotBlank
    private String lastName;
    @Email
    @NotBlank
    private String email;
    @Password
    @NotBlank
    private String password;

    private Image profileImage;

    @Size(min = 3, max = 15)
    private String phoneNumber;
}
