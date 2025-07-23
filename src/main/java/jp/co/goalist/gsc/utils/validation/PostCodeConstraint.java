package jp.co.goalist.gsc.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PostCodeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PostCodeConstraint {

    String message() default "郵便番号は「xxxxxxx」の形式で入力してください。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
