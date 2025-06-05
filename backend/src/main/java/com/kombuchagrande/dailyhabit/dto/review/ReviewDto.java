package com.kombuchagrande.dailyhabit.dto.review;

import com.kombuchagrande.dailyhabit.entity.enums.PeriodType;
import java.time.LocalDateTime;

public record ReviewDto(
    Long id,
    Long habitId,
    PeriodType periodType,
    int periodNumber,
    String title,
    String content,
    Integer emoji,
    String videoUrl,
    String resolution,
    LocalDateTime createdAt
) {

}
