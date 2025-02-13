package com.lms.onlinelms.usermanagement.service.interfaces;

import com.lms.onlinelms.usermanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IUserService {

    void checkIfUserIdCorrect( Long userId);

    Page<User> getAllUsers(PageRequest pageRequest);

    User getUserById(Long userId);
}
