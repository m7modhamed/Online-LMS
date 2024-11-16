package com.lms.onlinelms.usermanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.ResourceNotFoundException;
import com.lms.onlinelms.usermanagement.model.User;
import com.lms.onlinelms.usermanagement.repository.UserRepository;
import com.lms.onlinelms.usermanagement.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService implements IUserService {

    private final UserRepository userRepository;

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("User with email " + email + " not found" , HttpStatus.NOT_FOUND)
        );
    }
}
