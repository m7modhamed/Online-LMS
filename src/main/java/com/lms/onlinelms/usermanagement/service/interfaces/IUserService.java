package com.lms.onlinelms.usermanagement.service.interfaces;

import com.lms.onlinelms.usermanagement.model.User;

public interface IUserService {

    User getUserByEmail(String email);
}
