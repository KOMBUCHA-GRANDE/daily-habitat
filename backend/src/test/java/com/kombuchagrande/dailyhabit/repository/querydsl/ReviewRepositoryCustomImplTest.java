package com.kombuchagrande.dailyhabit.repository.querydsl;

import static org.assertj.core.api.Assertions.assertThat;

import com.kombuchagrande.dailyhabit.config.PagingProperties;
import com.kombuchagrande.dailyhabit.config.QuerydslConfig;
import com.kombuchagrande.dailyhabit.entity.Habit;
import com.kombuchagrande.dailyhabit.entity.Review;
import com.kombuchagrande.dailyhabit.entity.User;
import com.kombuchagrande.dailyhabit.entity.enums.PeriodType;
import com.kombuchagrande.dailyhabit.entity.enums.ProviderType;
import com.kombuchagrande.dailyhabit.enums.SortDirection;
import com.kombuchagrande.dailyhabit.repository.ReviewRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest
@EnableJpaAuditing
@Import(QuerydslConfig.class,)
@EnableConfigurationProperties(PagingProperties.class)
@ActiveProfiles("test")
class ReviewRepositoryCustomImplTest {

  @Autowired
  private PagingProperties pagingProperties;

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private ReviewRepositoryCustomImpl reviewRepositoryCustom;

  @Autowired
  private EntityManager em;

  private LocalDateTime fixedDate;
  private Habit habit;
  private Review r1;
  private Review r2;
  private Review r3;

  @BeforeEach
  void setUp() {
    fixedDate = LocalDateTime.of(2025, 9, 1, 0, 0, 0, 0);

    User user = User.builder()
        .nickname("testUser")
        .email("test@example.com")
        .providerType(ProviderType.GOOGLE)
        .notificationEnabled(true)
        .build();

    habit = Habit.builder()
        .user(user)
        .name("Daily Reading")
        .emoji(1)
        .startDate(LocalDateTime.now())
        .frequency("WEEKLY")
        .backgroundColor("#FFFFFF")
        .stackCount(0)
        .stackStartDate(LocalDate.now())
        .nextScheduledDate(LocalDate.now().plusDays(7))
        .isGraduated(false)
        .maxStackCount(30)
        .build();

    r1 = Review.builder()
        .habit(habit)
        .title("Title 1")
        .content("Content 1")
        .periodType(PeriodType.WEEKLY)
        .periodNumber(1)
        .emoji(1)
        .resolution("resolution")
        .startDate(LocalDate.now())
        .videoUrl("")
        .build();

    r2 = Review.builder()
        .habit(habit)
        .title("Title 2")
        .content("Content 2")
        .periodType(PeriodType.WEEKLY)
        .periodNumber(1)
        .emoji(1)
        .resolution("resolution")
        .startDate(LocalDate.now())
        .videoUrl("")
        .build();

    r3 = Review.builder()
        .habit(habit)
        .title("Keyword Match")
        .content("Content 3")
        .periodType(PeriodType.MONTHLY)
        .periodNumber(1)
        .emoji(1)
        .resolution("resolution")
        .startDate(LocalDate.now())
        .videoUrl("")
        .build();

    em.persist(user);
    em.persist(habit);
    em.persist(r1);
    em.persist(r2);
    em.persist(r3);

    ReflectionTestUtils.setField(r1, "createdAt", fixedDate);
    ReflectionTestUtils.setField(r2, "createdAt", fixedDate.plusDays(1));
    ReflectionTestUtils.setField(r3, "createdAt", fixedDate.plusDays(2));

    em.flush();
    em.clear();
  }

  @Test
  @DisplayName("lastIndex 없이 habitId, periodType, keyword 조건 조회")
  void findByCursor_basic() {
    // when
    List<Review> result = reviewRepositoryCustom.findByCursor(
        habit.getId(),
        PeriodType.WEEKLY,
        null,
        SortDirection.DESC,
        "Title",
        null,
        null,
        10
    );

    // then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getTitle()).isEqualTo("Title 2");
    assertThat(result.get(1).getTitle()).isEqualTo("Title 1");
  }

  @Test
  @DisplayName("lastIndex 조건 적용")
  void findByCursor_withLastIndex() {
    // r2의 id보다 작은 리뷰만 가져오기
    Long lastIndex = r2.getId();

    List<Review> result = reviewRepositoryCustom.findByCursor(
        habit.getId(),
        PeriodType.WEEKLY,
        lastIndex,
        SortDirection.DESC,
        null,
        null,
        null,
        10
    );

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTitle()).isEqualTo("Title 1");
  }

  @Test
  @DisplayName("createdBetween 조건 적용")
  void findByCursor_createdBetween() {
    LocalDate from = LocalDate.from(fixedDate.minusDays(1));
    LocalDate to = LocalDate.from(fixedDate.plusDays(1));

    List<Review> result = reviewRepositoryCustom.findByCursor(
        habit.getId(),
        PeriodType.WEEKLY,
        null,
        SortDirection.DESC,
        null,
        from,
        to,
        10
    );

    assertThat(result).hasSize(2);
  }
}
