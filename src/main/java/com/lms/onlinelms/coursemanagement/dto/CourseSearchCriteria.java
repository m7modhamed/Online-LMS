package com.lms.onlinelms.coursemanagement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseSearchCriteria {

    private String searchKey;

    private String language;

    private List<String> status;

    private List<String> category;

    private double minDuration;
    private double maxDuration;

/*    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime fromDateTime;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime toDateTime;*/
}
