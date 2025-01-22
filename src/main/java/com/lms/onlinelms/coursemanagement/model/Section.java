package com.lms.onlinelms.coursemanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String title;

    private String description;

    private short position;

    @ManyToOne(cascade = CascadeType.ALL)
    private Course course;

    @OneToMany(mappedBy = "section", fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private List<Lesson> lessons;
}
