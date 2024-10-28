package com.lms.onlinelms.usermanagement.dto;

import com.lms.onlinelms.usermanagement.model.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstructorSignupDto {


    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Image profileImage;
    private String phoneNumber;
    private String specialization;
    private String aboutMe;
    private String linkedinUrl;
    private String githubUrl;
    private String facebookUrl;
    private String twitterUrl;

}
