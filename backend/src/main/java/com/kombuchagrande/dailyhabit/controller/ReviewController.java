package com.kombuchagrande.dailyhabit.controller;

import com.kombuchagrande.dailyhabit.dto.review.ReviewCreateRequest;
import com.kombuchagrande.dailyhabit.dto.review.ReviewDto;
import com.kombuchagrande.dailyhabit.dto.review.ReviewDtoCursorResponse;
import com.kombuchagrande.dailyhabit.entity.enums.PeriodType;
import com.kombuchagrande.dailyhabit.enums.SortDirection;
import com.kombuchagrande.dailyhabit.service.ReviewService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping
  public ResponseEntity<ReviewDto> create(@Valid @RequestBody ReviewCreateRequest request) {
    ReviewDto reviewDto = reviewService.create(request);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(reviewDto);
  }

  @GetMapping("/{reviewId}")
  public ResponseEntity<ReviewDto> get(@PathVariable Long reviewId) {
    ReviewDto reviewDto = reviewService.get(reviewId);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(reviewDto);
  }

  @GetMapping
  public ResponseEntity<ReviewDtoCursorResponse> getReviews(
      @RequestParam Long habitId,
      @RequestParam PeriodType periodType,
      @RequestParam(required = false) Long lastIndex,
      @RequestParam(defaultValue = "DESC") SortDirection sortDirection,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
  ) {
    ReviewDtoCursorResponse response = reviewService.getReviews(habitId, periodType, lastIndex,
        sortDirection, keyword, from, to);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(response);
  }
}
