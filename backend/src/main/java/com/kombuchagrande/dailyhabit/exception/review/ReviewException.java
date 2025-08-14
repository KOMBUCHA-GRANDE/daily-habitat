package com.kombuchagrande.dailyhabit.exception.review;

import com.kombuchagrande.dailyhabit.exception.DailyHabitException;
import java.util.Map;

public class ReviewException extends DailyHabitException {

  public ReviewException(String message) {
    super(message);
  }

  public ReviewException(String message, Map<String, Object> details) {
    super(message, details);
  }
}
