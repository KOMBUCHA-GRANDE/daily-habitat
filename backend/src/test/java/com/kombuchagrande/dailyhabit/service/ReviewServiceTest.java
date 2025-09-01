package com.kombuchagrande.dailyhabit.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import com.kombuchagrande.dailyhabit.config.PagingProperties;
import com.kombuchagrande.dailyhabit.dto.review.ReviewCreateRequest;
import com.kombuchagrande.dailyhabit.dto.review.ReviewDto;
import com.kombuchagrande.dailyhabit.dto.review.ReviewDtoCursorResponse;
import com.kombuchagrande.dailyhabit.entity.Habit;
import com.kombuchagrande.dailyhabit.entity.Review;
import com.kombuchagrande.dailyhabit.entity.enums.PeriodType;
import com.kombuchagrande.dailyhabit.exception.review.ReviewNotFoundException;
import com.kombuchagrande.dailyhabit.mapper.ReviewMapper;
import com.kombuchagrande.dailyhabit.repository.HabitRepository;
import com.kombuchagrande.dailyhabit.repository.ReviewRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

@ExtendWith(MockitoExtension.class)
@Import(PagingProperties.class)
class ReviewServiceTest {

  @Mock
  private ReviewMapper reviewMapper;

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private HabitRepository habitRepository;

  @Mock
  private PagingProperties pagingProperties;

  @InjectMocks
  private ReviewService reviewService;

  private Long habitId;
  private Long reviewId;
  private Habit habit;
  private ReviewCreateRequest reviewCreateRequest;
  private Review review;
  private ReviewDto reviewDto;

  @BeforeEach
  void setUp() {
    habitId = 1L;
    habit = mock(Habit.class);
    reviewId = 1L;
    review = mock(Review.class);

    reviewCreateRequest = new ReviewCreateRequest(
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
        reviewCreateRequest.periodType(),
        reviewCreateRequest.periodNumber(),
        reviewCreateRequest.title(),
        reviewCreateRequest.content(),
        reviewCreateRequest.emoji(),
        "videoUrl",
        reviewCreateRequest.resolution(),
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
      ReviewDto result = reviewService.create(reviewCreateRequest);

      // then
      assertThat(result).isEqualTo(reviewDto);
      then(habitRepository).should(times(1)).findById(habitId);
      then(reviewRepository).should(times(1)).save(any(Review.class));
      then(reviewMapper).should(times(1)).toDto(review);
    }

    @Test
    @DisplayName("회고 생성 실패 - 존재하지 않는 습관 Id")
    // TODO: 습관 커스텀 예외로 변경
    void create_review_throwsIllegalArgumentException_whenHabitDoseNotExist() {
      // given
      given(habitRepository.findById(habitId)).willReturn(Optional.empty());

      // when & then
      assertThrows(IllegalArgumentException.class,
          () -> reviewService.create(reviewCreateRequest));
      then(habitRepository).should(times(1)).findById(habitId);
    }
  }

  @Nested
  @DisplayName("회고 상세 조회")
  public class GetReviewTest {

    @Test
    @DisplayName("회고 상세 조회 성공")
    void getReview_success() {
      // given
      given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));
      given(reviewMapper.toDto(review)).willReturn(reviewDto);

      // when
      ReviewDto result = reviewService.get(reviewId);

      // then
      assertThat(result).isNotNull();
      assertThat(result.id()).isEqualTo(reviewId);
      then(reviewRepository).should(times(1)).findById(reviewId);
      then(reviewMapper).should(times(1)).toDto(review);
    }

    @Test
    @DisplayName("회고 상세 조회 실패 - 존재하지 않는 회고 Id")
    void getReview_throwsReviewNotFoundException_whenReviewDoseNotExist() {
      // given
      given(reviewRepository.findById(reviewId)).willReturn(Optional.empty());

      // when & then
      assertThrows(ReviewNotFoundException.class,
          () -> reviewService.get(reviewId));
      then(reviewRepository).should(times(1)).findById(reviewId);
    }
  }

  @Nested
  @DisplayName("회고 전체 조회")
  public class GetReviewsTest {

    @Test
    @DisplayName("회고 전체 조회 성공 (첫 페이지) - hasNext true, size 1")
    void getReviews_success_hasNext_true_size1() {
      // given
      Review review1 = mock(Review.class);
      Review review2 = mock(Review.class);

      given(habitRepository.findById(habitId)).willReturn(Optional.of(habit));
      given(pagingProperties.getDefaultSize()).willReturn(1);
      given(reviewRepository.findByCursor(any(), any(), any(), any(), any(), any(), any(), eq(2)))
          .willReturn(List.of(review1, review2));

      given(reviewMapper.toDto(review1)).willReturn(new ReviewDto(1L, 1L,  PeriodType.WEEKLY,
          1, "t1", "c1", 1, "", "r1", LocalDateTime.now()));

      // when
      ReviewDtoCursorResponse response = reviewService.getReviews(
          1L, PeriodType.WEEKLY, null, null, null, null, null);

      // then
      assertThat(response.hasNext()).isTrue();
      assertThat(response.lastIndex()).isEqualTo(1L);
    }

    @Test
    @DisplayName("회고 전체 조회 성공 (마지막 페이지) - hasNext false, size 1")
    void getReviews_success_hasNext_false_size1() {
      // given
      Review review1 = mock(Review.class);

      given(habitRepository.findById(habitId)).willReturn(Optional.of(habit));
      given(pagingProperties.getDefaultSize()).willReturn(1);
      given(reviewRepository.findByCursor(any(), any(), any(), any(), any(), any(), any(), eq(2)))
          .willReturn(List.of(review1));

      given(reviewMapper.toDto(review1)).willReturn(new ReviewDto(1L, 1L,  PeriodType.WEEKLY,
          1, "t1", "c1", 1, "", "r1", LocalDateTime.now()));

      // when
      ReviewDtoCursorResponse response = reviewService.getReviews(
          1L, PeriodType.WEEKLY, null, null, null, null, null);

      // then
      assertThat(response.hasNext()).isFalse();
      assertThat(response.lastIndex()).isEqualTo(1L);
    }

    @Test
    @DisplayName("회고 전체 조회 - 데이터 없으면 빈 리스트 반환")
    void getReviews_emptyList() {
      // given
      given(habitRepository.findById(habitId)).willReturn(Optional.of(habit));
      given(pagingProperties.getDefaultSize()).willReturn(1);
      given(reviewRepository.findByCursor(any(), any(), any(), any(), any(), any(), any(), eq(2)))
          .willReturn(List.of());

      // when
      ReviewDtoCursorResponse response = reviewService.getReviews(
          1L, null, null, null, null, null, null);

      // then
      assertThat(response.hasNext()).isFalse();
      assertThat(response.lastIndex()).isNull();
      assertThat(response.content())
          .asInstanceOf(InstanceOfAssertFactories.list(ReviewDto.class))
          .isEmpty();
    }

    @Test
    @DisplayName("회고 전체 조회 실패 - 존재하지 않는 습관 Id")
    // TODO: 습관 커스텀 예외로 변경
    void getReviews_throwsIllegalArgumentException_whenHabitDoseNotExist() {
      // given
      given(habitRepository.findById(habitId)).willReturn(Optional.empty());

      // when & then
      assertThrows(IllegalArgumentException.class,
          () -> reviewService.getReviews(habitId, null,
              null, null, null, null, null));
      then(habitRepository).should(times(1)).findById(habitId);
    }
  }
}