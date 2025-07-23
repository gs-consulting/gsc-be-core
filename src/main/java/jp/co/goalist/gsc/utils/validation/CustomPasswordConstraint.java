package jp.co.goalist.gsc.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomPasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomPasswordConstraint {

    String message() default "パスワードには大文字・小文字・数字・記号を最低1文字以上を入力してください。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
