package com.yuseogi.common.validation.validator;

import com.yuseogi.common.validation.constraints.AllowedStringValues;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class AllowedStringValuesValidator implements ConstraintValidator<AllowedStringValues, String> {
    private String[] allowedValues;

    @Override
    public void initialize(AllowedStringValues constraintAnnotation) {
        this.allowedValues = constraintAnnotation.allowedValues();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return Arrays.asList(allowedValues).contains(value);
    }
}
