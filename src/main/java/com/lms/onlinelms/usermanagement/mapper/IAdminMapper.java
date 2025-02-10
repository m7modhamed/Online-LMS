package com.lms.onlinelms.usermanagement.mapper;


import com.lms.onlinelms.usermanagement.dto.AdminUpdateDto;
import com.lms.onlinelms.usermanagement.model.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IAdminMapper {
    AdminUpdateDto toAdminDto(Admin admin);
}
