package com.kombuchagrande.dailyhabit.entity;

import com.kombuchagrande.dailyhabit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "habit_frequency_period")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HabitFrequencyPeriod extends BaseEntity {

    @Column(nullable = false)
    private Long habitId;

    @Column(nullable = false, length = 7)
    private String frequency;

    /** 적용 시작일 */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** 적용 종료일, NULL=현재 오픈 구간 */
    @Column(name = "end_date")
    private LocalDate endDate;
}
