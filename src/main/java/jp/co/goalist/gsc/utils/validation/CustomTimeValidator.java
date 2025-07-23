package jp.co.goalist.gsc.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jp.co.goalist.gsc.common.Constants.TIME_RULE;

public class CustomTimeValidator implements
        ConstraintValidator<CustomTimeConstraint, String> {

    @Override
    public void initialize(CustomTimeConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(s)) {
            return true;
        }

        //HH:mm-HH:mm
        Pattern p = Pattern.compile(TIME_RULE);
        if (s == null || s.isBlank()) {
            return false;
        }

        Matcher m = p.matcher(s);
        return m.matches();
    }
}