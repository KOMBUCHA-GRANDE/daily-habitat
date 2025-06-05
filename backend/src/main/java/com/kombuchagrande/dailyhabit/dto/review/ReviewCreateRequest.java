package com.kombuchagrande.dailyhabit.dto.review;

import com.kombuchagrande.dailyhabit.entity.enums.PeriodType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReviewCreateRequest(

    @NotNull
    Long habitId,

    @NotNull
    PeriodType periodType,

    @Min(value = 1)
    int periodNumber,

    @NotNull
    LocalDate startDate,

    @NotBlank
    String title,

    @NotBlank
    String content,

    Integer emoji,

    @NotBlank
    String resolution
) {

}
