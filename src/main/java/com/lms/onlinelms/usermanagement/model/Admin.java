package com.lms.onlinelms.usermanagement.model;


import com.lms.onlinelms.coursemanagement.model.Course;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Admin extends User {

    @OneToMany(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private List<Course> reviewedCourses;

}
