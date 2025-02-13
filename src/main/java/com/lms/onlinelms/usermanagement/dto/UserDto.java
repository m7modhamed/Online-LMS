package com.lms.onlinelms.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.onlinelms.usermanagement.model.Image;
import com.lms.onlinelms.usermanagement.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {


    private Long id;

    private String firstName;


    private String lastName;


    private String email;


    private Image profileImage;

    private String phoneNumber;


    private Boolean isActive;


    private Boolean isBlocked;

    private Role role;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime lastUpdated;


}
