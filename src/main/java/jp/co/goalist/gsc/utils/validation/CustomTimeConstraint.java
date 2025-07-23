package jp.co.goalist.gsc.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomTimeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomTimeConstraint {

    String message() default "「HH:mm-HH:mm」形式で入力してください。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
