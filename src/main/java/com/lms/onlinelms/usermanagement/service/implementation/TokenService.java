package com.lms.onlinelms.usermanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.common.exceptions.ResourceNotFoundException;
import com.lms.onlinelms.usermanagement.model.User;
import com.lms.onlinelms.usermanagement.model.Token;
import com.lms.onlinelms.usermanagement.repository.VerificationTokenRepository;
import com.lms.onlinelms.usermanagement.service.interfaces.ITokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Calendar;

/**
 * @author Sampson Alfred
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TokenService implements ITokenService {
    private final VerificationTokenRepository tokenRepository;


    public Token validateToken(String theToken) {
       Token token = getTokenById(theToken);


        if (token.isUsed()) {
            throw new AppException( "This token has already been used." ,HttpStatus.BAD_REQUEST);
        }

        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            throw new AppException( "This token expired." ,HttpStatus.BAD_REQUEST);
        }
        return token;
    }

    private Token getTokenById(String theToken) {
        return tokenRepository.findByToken(theToken).orElseThrow(
                () -> new ResourceNotFoundException( "The token could not be found." ,HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public void saveVerificationTokenForUser(User user, String token) {
        var verificationToken = new Token(token, user);
        tokenRepository.save(verificationToken);
    }

    @Override
    public void saveToken(Token tokenObj) {
        tokenRepository.save(tokenObj);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordResetToken) {
        Token resetToken = new Token(passwordResetToken , user);
        tokenRepository.save(resetToken);
    }

}
