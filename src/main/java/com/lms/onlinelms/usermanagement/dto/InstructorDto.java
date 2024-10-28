package com.lms.onlinelms.usermanagement.dto;

import com.lms.onlinelms.usermanagement.model.Image;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InstructorDto {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private Image profileImage;
    private String phoneNumber;
    private String specialization;
    private String aboutMe;
    private String linkedinUrl;
    private String githubUrl;
    private String facebookUrl;
    private String twitterUrl;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
}
