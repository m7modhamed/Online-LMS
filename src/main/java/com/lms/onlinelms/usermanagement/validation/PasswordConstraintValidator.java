package com.lms.onlinelms.usermanagement.validation;

import com.lms.onlinelms.usermanagement.validation.customAnnotations.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;

public class PasswordConstraintValidator implements
        ConstraintValidator<Password,String> {
    @Override
    public void initialize(Password constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }



    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator passwordValidator = new PasswordValidator(
                Arrays.asList(
                        //Length rule. Min 10 max 128 characters
                        new LengthRule(10, 128),
                        //At least one upper case letter
                        new CharacterRule(EnglishCharacterData.UpperCase, 1),
                        //At least one lower case letter
                        new CharacterRule(EnglishCharacterData.LowerCase, 1),
                        //At least one number
                        new CharacterRule(EnglishCharacterData.Digit, 1),
                        //At least one special characters
                        new CharacterRule(EnglishCharacterData.Special, 1),
                        new WhitespaceRule()
                )
        );

        RuleResult result = passwordValidator.validate(new PasswordData(password));


        return result.isValid();
    }
}
