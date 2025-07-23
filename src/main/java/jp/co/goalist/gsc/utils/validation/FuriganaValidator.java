package jp.co.goalist.gsc.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jp.co.goalist.gsc.common.Constants.FURIGANA_RULE;

public class FuriganaValidator implements ConstraintValidator<FuriganaConstraint, String> {

    @Override
    public void initialize(FuriganaConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String furigana, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(furigana) || furigana.isEmpty()) {
            return true;
        }

        Pattern p = Pattern.compile(FURIGANA_RULE);
        Matcher m = p.matcher(furigana);
        return m.matches();
    }
}