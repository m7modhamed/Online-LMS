package com.lms.onlinelms.usermanagement.service.interfaces;

import com.lms.onlinelms.usermanagement.model.Token;
import com.lms.onlinelms.usermanagement.model.User;

/**
 * @author Sampson Alfred
 */

public interface ITokenService {
    void saveVerificationTokenForUser(User user, String token);

    void createPasswordResetTokenForUser(User user, String passwordResetToken);

    Token validateToken(String theToken);

    void saveToken(Token tokenObj);
}
