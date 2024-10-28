package com.lms.onlinelms.common.utility;

import com.lms.onlinelms.usermanagement.model.Admin;
import com.lms.onlinelms.usermanagement.model.Role;
import com.lms.onlinelms.usermanagement.repository.AdminRepository;
import com.lms.onlinelms.usermanagement.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.CharBuffer;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;


    @Override
    public void run(String... args)  {

        Role studentRole;
        Role instructorRole;
        Role adminRole;

        Optional<Role> roleStudentOptional = roleRepository.findByName("ROLE_STUDENT");
        if (roleStudentOptional.isEmpty()) {
            studentRole = new Role();
            studentRole.setName("ROLE_STUDENT");
            studentRole.setDescription("Student role");
            roleRepository.save(studentRole);
        }

        Optional<Role> roleInstructorOptional = roleRepository.findByName("ROLE_INSTRUCTOR");
        if (roleInstructorOptional.isEmpty()) {
            instructorRole = new Role();
            instructorRole.setName("ROLE_INSTRUCTOR");
            instructorRole.setDescription("Instructor role");
            roleRepository.save(instructorRole);
        }

        Optional<Role> roleAdminOptional = roleRepository.findByName("ROLE_ADMIN");
        if (roleAdminOptional.isEmpty()) {
            adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setDescription("Admin role");
            roleRepository.save(adminRole);
        }else {
            adminRole=roleAdminOptional.get();
        }


        Optional<Admin> adminOptional=adminRepository.findByEmail("m7modm42@gmail.com");
        if(adminOptional.isEmpty()) {
            Admin admin = new Admin();
            admin.setEmail("m7modm42@gmail.com");
            admin.setMyPassword(passwordEncoder.encode(CharBuffer.wrap("123")));
            admin.setFirstName("mahmoud");
            admin.setLastName("hamed");
            admin.setIsActive(true);
            admin.setIsBlocked(false);
            admin.setRole(adminRole);
            adminRepository.save(admin);
        }
    }
}

