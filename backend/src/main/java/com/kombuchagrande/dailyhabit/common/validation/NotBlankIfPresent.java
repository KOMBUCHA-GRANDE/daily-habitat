package com.kombuchagrande.dailyhabit.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotBlankIfPresentValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankIfPresent {
    String message() default "공백 문자열은 허용되지 않습니다.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}