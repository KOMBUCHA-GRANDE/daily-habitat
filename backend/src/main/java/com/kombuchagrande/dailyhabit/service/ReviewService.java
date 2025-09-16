package com.kombuchagrande.dailyhabit.service;

import com.kombuchagrande.dailyhabit.config.PagingProperties;
import com.kombuchagrande.dailyhabit.dto.review.ReviewCreateRequest;
import com.kombuchagrande.dailyhabit.dto.review.ReviewDto;
import com.kombuchagrande.dailyhabit.dto.review.ReviewDtoCursorResponse;
import com.kombuchagrande.dailyhabit.entity.Habit;
import com.kombuchagrande.dailyhabit.entity.Review;
import com.kombuchagrande.dailyhabit.entity.enums.PeriodType;
import com.kombuchagrande.dailyhabit.enums.SortDirection;
import com.kombuchagrande.dailyhabit.exception.review.ReviewNotFoundException;
import com.kombuchagrande.dailyhabit.mapper.ReviewMapper;
import com.kombuchagrande.dailyhabit.repository.HabitRepository;
import com.kombuchagrande.dailyhabit.repository.ReviewRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewMapper reviewMapper;
  private final ReviewRepository reviewRepository;

  private final HabitRepository habitRepository;

  private final PagingProperties pagingProperties;

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

  @Transactional(readOnly = true)
  public ReviewDto get(Long reviewId) {
    Review review = getReviewOrThrow(reviewId);

    return reviewMapper.toDto(review);
  }

  @Transactional(readOnly = true)
  public ReviewDtoCursorResponse getReviews(
      Long habitId,
      PeriodType periodType,
      Long lastIndex,
      SortDirection sortDirection,
      String keyword,
      LocalDate from,
      LocalDate to
  ) {
    int size = pagingProperties.defaultSize();
    getHabitOrThrow(habitId);

    List<Review> reviews = reviewRepository.findByCursor(
        habitId,
        periodType,
        lastIndex,
        sortDirection,
        keyword,
        from,
        to,
        size + 1
    );

    List<ReviewDto> reviewDtos = reviews.stream()
        .limit(size)
        .map(reviewMapper::toDto)
        .toList();
    boolean hasNext = reviews.size() > size;
    Long nextLastIndex = reviewDtos.isEmpty() ? null : reviewDtos.get(reviewDtos.size() - 1).id();

    return new ReviewDtoCursorResponse(reviewDtos, nextLastIndex, hasNext);
  }

  private Review getReviewOrThrow(Long reviewId) {
    return reviewRepository.findById(reviewId)
        .orElseThrow(() -> ReviewNotFoundException.withId(reviewId));
  }

  // TODO: Habit 커스텀 예외로 변경
  private Habit getHabitOrThrow(Long habitId) {
    return habitRepository.findById(habitId)
        .orElseThrow(IllegalArgumentException::new);
  }
}
