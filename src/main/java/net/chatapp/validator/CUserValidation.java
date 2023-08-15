package net.chatapp.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CUserValidator.class)
public @interface CUserValidation {
    String message() default "Error!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
