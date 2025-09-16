package com.kombuchagrande.dailyhabit.exception.review;

import com.kombuchagrande.dailyhabit.exception.ErrorCode;

public class ReviewNotFoundException extends ReviewException {

  public ReviewNotFoundException() {
    super(ErrorCode.REVIEW_NOT_FOUND.getMessage());
  }

  public static ReviewNotFoundException withId(Long id) {
    ReviewNotFoundException exception = new ReviewNotFoundException();
    exception.addDetail("id", id);
    return exception;
  }
}
