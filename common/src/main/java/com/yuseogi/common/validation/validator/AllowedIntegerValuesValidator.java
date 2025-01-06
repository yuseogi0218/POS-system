package com.yuseogi.common.validation.validator;

import com.yuseogi.common.validation.constraints.AllowedIntegerValues;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class AllowedIntegerValuesValidator implements ConstraintValidator<AllowedIntegerValues, Integer> {
    private int[] allowedValues;

    @Override
    public void initialize(AllowedIntegerValues constraintAnnotation) {
        this.allowedValues = constraintAnnotation.allowedValues();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Arrays.stream(allowedValues).anyMatch(v -> v == value);
    }
}
