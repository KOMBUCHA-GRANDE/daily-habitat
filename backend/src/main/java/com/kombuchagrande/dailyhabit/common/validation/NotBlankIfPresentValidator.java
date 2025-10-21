package com.kombuchagrande.dailyhabit.common.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankIfPresentValidator
        implements ConstraintValidator<NotBlankIfPresent, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return !value.toString().trim().isEmpty();
    }
}