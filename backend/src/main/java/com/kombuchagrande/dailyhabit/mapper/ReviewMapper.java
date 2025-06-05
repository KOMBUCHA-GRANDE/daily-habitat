package com.kombuchagrande.dailyhabit.mapper;

import com.kombuchagrande.dailyhabit.dto.review.ReviewDto;
import com.kombuchagrande.dailyhabit.entity.Review;

public class ReviewMapper {

  public static ReviewDto toDto(Review review) {
    if (review == null) {
      return null;
    }

    return new ReviewDto(
        review.getId(),
        review.getHabit().getId(),
        review.getPeriodType(),
        review.getPeriodNumber(),
        review.getTitle(),
        review.getContent(),
        review.getEmoji(),
        review.getVideoUrl(),
        review.getResolution(),
        review.getCreatedAt()
    );
  }

}
