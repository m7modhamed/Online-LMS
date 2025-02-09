package com.lms.onlinelms.usermanagement.dto;

import com.lms.onlinelms.usermanagement.model.Image;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstructorUpdateDto {

    @Size(min = 1, max = 15)
    @NotBlank
    private String firstName;

    @Size(min = 1, max = 15)
    @NotBlank
    private String lastName;


    private Image profileImage;

    @Size(min = 1, max = 15)
    private String phoneNumber;

    @NotBlank
    @Size(min = 3, max = 25)
    private String specialization;

    @NotBlank
    @Size(min = 25, max = 500)
    private String aboutMe;

    @URL(host = "www.linkedin.com")
    private String linkedinUrl;

    @URL(host = "www.github.com")
    private String githubUrl;

    @URL(host = "www.facebook.com")
    private String facebookUrl;

    @URL(host = "www.x.com")
    private String twitterUrl;

}
