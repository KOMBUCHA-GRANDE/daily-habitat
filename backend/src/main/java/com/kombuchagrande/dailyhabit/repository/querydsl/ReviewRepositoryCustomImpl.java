package com.kombuchagrande.dailyhabit.repository.querydsl;

import static com.kombuchagrande.dailyhabit.entity.QReview.review;

import com.kombuchagrande.dailyhabit.entity.Review;
import com.kombuchagrande.dailyhabit.entity.enums.PeriodType;
import com.kombuchagrande.dailyhabit.enums.SortDirection;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Review> findByCursor(
      Long habitId,
      PeriodType periodType,
      Long lastIndex,
      SortDirection sortDirection,
      String keyword,
      LocalDate from,
      LocalDate to,
      int size
  ) {
    return jpaQueryFactory
        .selectFrom(review)
        .where(
            habitIdEq(habitId),
            periodTypeEq(periodType),
            keywordContains(keyword),
            createdBetween(from, to),
            lastIndexCondition(lastIndex, sortDirection)
        )
        .orderBy(sortDirection == SortDirection.DESC
            ? review.createdAt.desc()
            : review.createdAt.asc())
        .limit(size)
        .fetch();
  }

  private BooleanExpression habitIdEq(Long habitId) {
    return habitId != null ? review.habit.id.eq(habitId) : null;
  }

  private BooleanExpression periodTypeEq(PeriodType periodType) {
    return periodType != null ? review.periodType.eq(periodType) : null;
  }

  private BooleanExpression keywordContains(String keyword) {
    return keyword != null ? review.title.containsIgnoreCase(keyword) : null;
  }

  private BooleanExpression createdBetween(LocalDate from, LocalDate to) {
    if (from == null || to == null) return null;
    return review.createdAt.between(from.atStartOfDay(), to.atTime(23, 59, 59));
  }

  private BooleanExpression lastIndexCondition(Long lastIndex, SortDirection sortDirection) {
    if (lastIndex == null) return null;
    return sortDirection == SortDirection.DESC
        ? review.id.lt(lastIndex)
        : review.id.gt(lastIndex);
  }
}
