package jp.co.goalist.gsc.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jp.co.goalist.gsc.common.Constants.POSTCODE_RULE;

public class PostCodeValidator implements ConstraintValidator<PostCodeConstraint, String> {

    @Override
    public void initialize(PostCodeConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String postCode, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(postCode) || postCode.isEmpty()) {
            return true;
        }

        Pattern p = Pattern.compile(POSTCODE_RULE);
        Matcher m = p.matcher(postCode);
        return m.matches();
    }
}
