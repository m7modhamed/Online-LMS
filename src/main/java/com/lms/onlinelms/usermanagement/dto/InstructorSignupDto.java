package com.lms.onlinelms.usermanagement.dto;

import com.lms.onlinelms.usermanagement.model.Image;
import com.lms.onlinelms.usermanagement.validation.customAnnotations.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstructorSignupDto {

    @Size(min = 1, max = 15)
    @NotBlank
    private String firstName;

    @Size(min = 1, max = 15)
    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @Password
    @NotBlank
    private String password;

    private Image profileImage;

    @Size(min = 1, max = 15)
    private String phoneNumber;

    @NotBlank
    private String specialization;

    @NotBlank
    private String aboutMe;

    @URL(host = "www.linkedin.com")
    private String linkedinUrl;

    @URL(host = "github.com")
    private String githubUrl;

    @URL(host = "web.facebook.com")
    private String facebookUrl;

    @URL(host = "x.com")
    private String twitterUrl;

}
