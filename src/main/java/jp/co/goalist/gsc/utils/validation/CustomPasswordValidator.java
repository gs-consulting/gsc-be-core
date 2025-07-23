package jp.co.goalist.gsc.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jp.co.goalist.gsc.common.Constants.PASSWORD_RULE;

public class CustomPasswordValidator implements
        ConstraintValidator<CustomPasswordConstraint, String> {

    @Override
    public void initialize(CustomPasswordConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String pwd, ConstraintValidatorContext constraintValidatorContext) {
        Pattern p = Pattern.compile(PASSWORD_RULE);
        if (pwd == null || pwd.isBlank()) {
            return false;
        }

        Matcher m = p.matcher(pwd);
        return m.matches();
    }
}
