package com.kombuchagrande.dailyhabit.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kombuchagrande.dailyhabit.dto.review.ReviewCreateRequest;
import com.kombuchagrande.dailyhabit.dto.review.ReviewDto;
import com.kombuchagrande.dailyhabit.entity.enums.PeriodType;
import com.kombuchagrande.dailyhabit.service.ReviewService;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
}