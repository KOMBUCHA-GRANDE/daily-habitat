package com.kombuchagrande.dailyhabit.repository.querydsl;

import com.kombuchagrande.dailyhabit.entity.Review;
import com.kombuchagrande.dailyhabit.entity.enums.PeriodType;
import com.kombuchagrande.dailyhabit.enums.SortDirection;
import java.time.LocalDate;
import java.util.List;

public interface ReviewRepositoryCustom {
  List<Review> findByCursor(
      Long habitId,
      PeriodType periodType,
      Long lastIndex,
      SortDirection sortDirection,
      String keyword,
      LocalDate from,
      LocalDate to,
      int size
  );
}
