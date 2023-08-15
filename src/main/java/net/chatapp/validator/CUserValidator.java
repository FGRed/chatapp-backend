package net.chatapp.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CUserValidator implements ConstraintValidator<CUserValidation, String> {
    @Override
    public void initialize(CUserValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if(s.equals("username")){



        }

        return false;
    }
}
