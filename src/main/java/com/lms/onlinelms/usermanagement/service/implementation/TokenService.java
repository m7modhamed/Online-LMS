package com.lms.onlinelms.usermanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.usermanagement.model.User;
import com.lms.onlinelms.usermanagement.model.Token;
import com.lms.onlinelms.usermanagement.repository.VerificationTokenRepository;
import com.lms.onlinelms.usermanagement.service.interfaces.ITokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

/**
 * @author Sampson Alfred
 */
@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    private final VerificationTokenRepository tokenRepository;


    public Token validateToken(String theToken) {
        Optional<Token> token = tokenRepository.findByToken(theToken);
        if (token.isEmpty()) {
            throw new AppException( "The token could not be found." ,HttpStatus.NOT_FOUND);
        }

        if (token.get().isUsed()) {
            throw new AppException( "This token has already been used." ,HttpStatus.BAD_REQUEST);
        }

        Calendar calendar = Calendar.getInstance();
        if ((token.get().getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            throw new AppException( "This token expired." ,HttpStatus.BAD_REQUEST);
        }
        return token.get();
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
