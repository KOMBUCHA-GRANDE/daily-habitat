package com.kombuchagrande.dailyhabit.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.kombuchagrande.dailyhabit.dto.review.ReviewCreateRequest;
import com.kombuchagrande.dailyhabit.dto.review.ReviewDto;
import com.kombuchagrande.dailyhabit.entity.Habit;
import com.kombuchagrande.dailyhabit.entity.Review;
import com.kombuchagrande.dailyhabit.entity.enums.PeriodType;
import com.kombuchagrande.dailyhabit.mapper.ReviewMapper;
import com.kombuchagrande.dailyhabit.repository.HabitRepository;
import com.kombuchagrande.dailyhabit.repository.ReviewRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

  @Mock
  private ReviewMapper reviewMapper;

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private HabitRepository habitRepository;

  @InjectMocks
  private ReviewService reviewService;

  private Long habitId;
  private Long reviewId;
  private Habit habit;
  private ReviewCreateRequest request;
  private Review review;
  private ReviewDto reviewDto;

  @BeforeEach
  void setUp() {
    habitId = 1L;
    habit = mock(Habit.class);
    reviewId = 1L;
    review = mock(Review.class);

    request = new ReviewCreateRequest(
        habitId,
        PeriodType.WEEKLY,
        2,
        LocalDate.of(2025, 8, 1),
        "title",
        "content",
        1,
        "resolution"
    );

    reviewDto = new ReviewDto(
        reviewId,
        habitId,
        request.periodType(),
        request.periodNumber(),
        request.title(),
        request.content(),
        request.emoji(),
        "videoUrl",
        request.resolution(),
        LocalDateTime.now()
    );
  }

  @Nested
  @DisplayName("회고 생성")
  public class CreateReviewTest {

    @Test
    @DisplayName("회고 생성 성공")
    void create_review_success() {
      // given
      given(habitRepository.findById(habitId)).willReturn(Optional.of(habit));
      given(reviewRepository.save(any(Review.class))).willReturn(review);
      given(reviewMapper.toDto(review)).willReturn(reviewDto);

      // when
      ReviewDto result = reviewService.create(request);

      // then
      assertThat(result).isEqualTo(reviewDto);
      then(habitRepository).should(times(1)).findById(habitId);
      then(reviewRepository).should(times(1)).save(any(Review.class));
      then(reviewMapper).should(times(1)).toDto(review);
    }
  }
}