package com.kombuchagrande.dailyhabit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kombuchagrande.dailyhabit.dto.review.ReviewCreateRequest;
import com.kombuchagrande.dailyhabit.dto.review.ReviewDto;
import com.kombuchagrande.dailyhabit.dto.review.ReviewDtoCursorResponse;
import com.kombuchagrande.dailyhabit.entity.enums.PeriodType;
import com.kombuchagrande.dailyhabit.service.ReviewService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ReviewService reviewService;

  private Long habitId;
  private Long reviewId;
  private ReviewCreateRequest reviewCreateRequest;
  private ReviewDto reviewDto;
  private ReviewDtoCursorResponse cursorResponse;

  @BeforeEach
  void setUp() {
    habitId = 1L;
    reviewId = 1L;

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

    cursorResponse = new ReviewDtoCursorResponse(
        List.of(reviewDto),
        1L,
        true
    );
  }

  @Nested
  @DisplayName("회고 생성")
  public class CreateReviewTest {

    @Test
    @DisplayName("회고 생성 성공")
    void create_review_success() throws Exception {
      // given
      given(reviewService.create(any(ReviewCreateRequest.class))).willReturn(reviewDto);

      // when & then
      mockMvc.perform(post("/api/reviews")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(reviewCreateRequest)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id").value(1L))
          .andExpect(jsonPath("$.habitId").value(1L))
          .andExpect(jsonPath("$.title").value("title"))
          .andExpect(jsonPath("$.content").value("content"))
          .andExpect(jsonPath("$.emoji").value(1))
          .andExpect(jsonPath("$.resolution").value("resolution"));
    }
  }

  @Nested
  @DisplayName("회고 상세 조회")
  public class GetReviewTest {

    @Test
    @DisplayName("회고 상세 조회 성공")
    void get_review_success() throws Exception {
      // given
      given(reviewService.get(reviewId)).willReturn(reviewDto);

      // when & then
      mockMvc.perform(get("/api/reviews/{reviewId}", reviewId)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1L))
          .andExpect(jsonPath("$.habitId").value(1L))
          .andExpect(jsonPath("$.title").value("title"))
          .andExpect(jsonPath("$.content").value("content"))
          .andExpect(jsonPath("$.emoji").value(1))
          .andExpect(jsonPath("$.resolution").value("resolution"));
    }
  }

  @Nested
  @DisplayName("회고 전체 조회")
  public class GetReviewsTest {

    @Test
    @DisplayName("회고 전체 조회 성공")
    void getReviews_success() throws Exception {
      // given
      given(reviewService.getReviews(
          eq(1L),
          eq(PeriodType.WEEKLY),
          any(),
          any(),
          any(),
          any(),
          any()
      )).willReturn(cursorResponse);

      // when & then
      mockMvc.perform(get("/api/reviews")
              .param("habitId", "1")
              .param("periodType", "WEEKLY")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content[0].id").value(1))
          .andExpect(jsonPath("$.lastIndex").value(1))
          .andExpect(jsonPath("$.hasNext").value(true));
    }
  }
}