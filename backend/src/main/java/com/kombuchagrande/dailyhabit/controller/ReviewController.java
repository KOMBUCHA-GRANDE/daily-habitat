package com.kombuchagrande.dailyhabit.controller;

import com.kombuchagrande.dailyhabit.dto.review.ReviewCreateRequest;
import com.kombuchagrande.dailyhabit.dto.review.ReviewDto;
import com.kombuchagrande.dailyhabit.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
