package jp.co.goalist.gsc.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailConstraint {

    String message() default "※有効なメールアドレスを入力してください。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
