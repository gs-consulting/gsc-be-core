package jp.co.goalist.gsc.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jp.co.goalist.gsc.common.Constants.EMAIL_RULE;

public class EmailValidator implements ConstraintValidator<EmailConstraint, String> {

    @Override
    public void initialize(EmailConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(email) || email.isEmpty()) {
            return true;
        }

        Pattern p = Pattern.compile(EMAIL_RULE);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
