package com.kombuchagrande.dailyhabit.service;

import com.kombuchagrande.dailyhabit.dto.review.ReviewCreateRequest;
import com.kombuchagrande.dailyhabit.dto.review.ReviewDto;
import com.kombuchagrande.dailyhabit.entity.Habit;
import com.kombuchagrande.dailyhabit.entity.Review;
import com.kombuchagrande.dailyhabit.mapper.ReviewMapper;
import com.kombuchagrande.dailyhabit.repository.HabitRepository;
import com.kombuchagrande.dailyhabit.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewMapper reviewMapper;
  private final ReviewRepository reviewRepository;

  private final HabitRepository habitRepository;

  @Transactional
  public ReviewDto create(ReviewCreateRequest request) {
    Habit habit = getHabitOrThrow(request.habitId());

    Review review = Review.builder()
        .habit(habit)
        .periodType(request.periodType())
        .periodNumber(request.periodNumber())
        .startDate(request.startDate())
        .title(request.title())
        .content(request.content())
        .emoji(request.emoji())
        // TODO: videoUrl
        .videoUrl("videoUrl")
        .resolution(request.resolution())
        .build();
    Review savedReview = reviewRepository.save(review);

    return reviewMapper.toDto(savedReview);
  }

  // TODO: Habit 커스텀 예외로 변경
  private Habit getHabitOrThrow(Long habitId) {
    return habitRepository.findById(habitId)
        .orElseThrow(IllegalArgumentException::new);
  }
}
