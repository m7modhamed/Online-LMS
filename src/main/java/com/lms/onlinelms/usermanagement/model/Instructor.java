package com.lms.onlinelms.usermanagement.model;

import jakarta.persistence.Entity;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Instructor extends User {

    private String specialization;

    private String aboutMe;

    private String linkedinUrl;

    private String githubUrl;

    private String facebookUrl;

    private String twitterUrl;
}
