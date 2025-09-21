package com.kombuchagrande.dailyhabit.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class DailyHabitException extends RuntimeException {

    private final Map<String, Object> details;

    public DailyHabitException(String message) {
        super(message);
        this.details = new HashMap<>();
    }

    public DailyHabitException(String message, Map<String, Object> details) {
        super(message);
        this.details = details;
    }

    public void addDetail(String key, Object value) {
        this.details.put(key, value);
    }
}
