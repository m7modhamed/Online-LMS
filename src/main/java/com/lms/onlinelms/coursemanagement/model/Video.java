package com.lms.onlinelms.coursemanagement.model;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Video extends Content{


    private double durationInSecond;
}
