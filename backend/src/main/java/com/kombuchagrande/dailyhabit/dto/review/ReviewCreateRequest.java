package com.kombuchagrande.dailyhabit.dto.review;

import com.kombuchagrande.dailyhabit.entity.enums.PeriodType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReviewCreateRequest(

    @NotNull(message = "habitId는 필수입니다.")
    Long habitId,

    @NotNull(message = "periodType은 필수입니다.")
    PeriodType periodType,

    @Min(value = 1, message = "periodNumber는 1 이상이어야 합니다.")
    int periodNumber,

    @NotNull(message = "startDate 필수입니다.")
    LocalDate startDate,

    @NotBlank(message = "title은 필수입니다..")
    String title,

    @NotBlank(message = "content는 필수입니다.")
    String content,

    Integer emoji,

    @NotBlank(message = "resolution은 필수입니다.")
    String resolution
) {

}

