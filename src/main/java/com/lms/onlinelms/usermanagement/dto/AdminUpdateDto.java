package com.lms.onlinelms.usermanagement.dto;

import com.lms.onlinelms.usermanagement.model.Image;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateDto {


    @Size(min = 3, max = 15)
    @NotBlank
    private String firstName;

    @Size(min = 3, max = 15)
    @NotBlank
    private String lastName;

    private String email;

    private Image profileImage;

    @Size(min = 3, max = 15)
    private String phoneNumber;
}
