package com.lms.onlinelms.usermanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.common.exceptions.ResourceNotFoundException;
import com.lms.onlinelms.common.utility.UserUtil;
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


    @Override
    // check user id is same with user that logged by token
    public void checkIfUserIdCorrect( Long userId){
        User user = UserUtil.getCurrentUser();

        if(!user.getId().equals(userId)){
            throw new AppException("the user id is not correct ,please try again.", HttpStatus.BAD_REQUEST);
        }
    }
}
