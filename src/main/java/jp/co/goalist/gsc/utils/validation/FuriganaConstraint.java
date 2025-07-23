package jp.co.goalist.gsc.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FuriganaValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FuriganaConstraint {

    String message() default "フリガナはカタカナのみで入力してください。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
