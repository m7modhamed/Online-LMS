package com.lms.onlinelms.coursemanagement.model;

import com.lms.onlinelms.coursemanagement.enums.CourseStatus;
import com.lms.onlinelms.usermanagement.model.Instructor;
import com.lms.onlinelms.usermanagement.model.Student;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.*;

@EqualsAndHashCode
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    //@Enumerated(EnumType.STRING)
    private CourseStatus status;

    private String language;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> prerequisites;

    @OneToMany(mappedBy = "course" ,fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private List<Section> sections;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY )
    private Instructor instructor;

    @ManyToMany(mappedBy = "courses")
    private List<Student> enrolledStudents;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime lastUpdate;




}
