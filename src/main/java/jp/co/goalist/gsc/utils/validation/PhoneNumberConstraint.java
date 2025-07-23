package jp.co.goalist.gsc.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumberConstraint {

    String message() default "※有効な電話番号を入力してください。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
