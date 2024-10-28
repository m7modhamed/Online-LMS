package com.lms.onlinelms.coursemanagement.controller;

import com.lms.onlinelms.coursemanagement.dto.SectionRequestDto;
import com.lms.onlinelms.coursemanagement.dto.SectionResponseDto;
import com.lms.onlinelms.coursemanagement.mapper.SectionMapper;
import com.lms.onlinelms.coursemanagement.model.Section;
import com.lms.onlinelms.coursemanagement.service.interfaces.ISectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class SectionController {

    private final ISectionService sectionService;
    private final SectionMapper sectionMapper;

    @PostMapping("/courses/{course_id}/sections")
    public ResponseEntity<SectionResponseDto> createCourseSection(
            @PathVariable("course_id") Long course_id
            , @RequestBody SectionRequestDto sectionRequestDto){

        Section section = sectionMapper.toSection(sectionRequestDto);

        Section appendedSection = sectionService.appendSection(course_id,section);

        SectionResponseDto sectionResponseDto = sectionMapper.toSectionResponseDto(appendedSection);
        return ResponseEntity.ok(sectionResponseDto);
    }


}
