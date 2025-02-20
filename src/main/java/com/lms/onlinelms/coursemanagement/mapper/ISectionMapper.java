package com.lms.onlinelms.coursemanagement.mapper;

import com.lms.onlinelms.coursemanagement.dto.SectionRequestDto;
import com.lms.onlinelms.coursemanagement.dto.SectionResponseDto;
import com.lms.onlinelms.coursemanagement.model.Section;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ISectionMapper {

List<SectionResponseDto> toSectionResponseDto(List<Section> sections);
    Section toSection(SectionRequestDto sectionRequestDto);


    SectionResponseDto toSectionResponseDto(Section section);
}
