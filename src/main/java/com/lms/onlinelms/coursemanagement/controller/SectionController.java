package com.lms.onlinelms.coursemanagement.controller;

import com.lms.onlinelms.coursemanagement.dto.SectionRequestDto;
import com.lms.onlinelms.coursemanagement.dto.SectionResponseDto;
import com.lms.onlinelms.coursemanagement.mapper.ISectionMapper;
import com.lms.onlinelms.coursemanagement.model.Section;
import com.lms.onlinelms.coursemanagement.service.interfaces.ISectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SectionController {

    private final ISectionService sectionService;
    private final ISectionMapper ISectionMapper;

    @PostMapping("/courses/{course_id}/sections")
    public ResponseEntity<SectionResponseDto> createSection(
            @PathVariable("course_id") Long course_id
            , @RequestBody @Valid SectionRequestDto sectionRequestDto){

        Section section = ISectionMapper.toSection(sectionRequestDto);

        Section appendedSection = sectionService.appendSection(course_id,section);

        SectionResponseDto sectionResponseDto = ISectionMapper.toSectionResponseDto(appendedSection);
        return ResponseEntity.ok(sectionResponseDto);
    }

    @GetMapping("/courses/{course_id}/sections")
    public ResponseEntity<List<SectionResponseDto>> getSections(
            @PathVariable("course_id") Long course_id){


        List<Section> sections = sectionService.getCourseSections(course_id);

        List<SectionResponseDto> sectionResponseDto = ISectionMapper.toSectionResponseDto(sections);
        return ResponseEntity.ok(sectionResponseDto);
    }


}
