package com.kombuchagrande.dailyhabit.mapper;

import com.kombuchagrande.dailyhabit.dto.review.ReviewDto;
import com.kombuchagrande.dailyhabit.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

  public ReviewDto toDto(Review review) {
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
