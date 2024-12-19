package com.lms.onlinelms.coursemanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FileResource extends Content{

    private String name;

    private String type;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JsonBackReference
    private Lesson lesson;

}
