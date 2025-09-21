package com.kombuchagrande.dailyhabit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  // review
  REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "회고를 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  ErrorCode(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }
}
