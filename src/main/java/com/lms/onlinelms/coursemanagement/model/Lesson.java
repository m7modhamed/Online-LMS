package com.lms.onlinelms.coursemanagement.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String title;

    private short position;

    @ManyToOne
    private Section section;

    @OneToMany(fetch = FetchType.EAGER ,mappedBy = "lesson", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<FileResource> fileResource;

    @OneToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private Video video;
}
