package jp.co.goalist.gsc.utils.validation;

import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.co.goalist.gsc.common.Constants;

public class FaxCodeValidator implements ConstraintValidator<FaxCodeConstraint, String> {

    @Override
    public void initialize(FaxCodeConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value) || value.isEmpty()) {
            return true;
        }

        // Check if the fax code matches the invalid pattern
        return value.matches(Constants.FAXCODE_RULE);
    }
}
