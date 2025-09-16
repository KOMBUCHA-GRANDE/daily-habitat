package com.kombuchagrande.dailyhabit.entity;

import com.kombuchagrande.dailyhabit.entity.base.BaseSoftDeletableEntity;
import com.kombuchagrande.dailyhabit.entity.enums.PeriodType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reviews")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseSoftDeletableEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodType periodType;

    @Column(nullable = false)
    private int periodNumber;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private Integer emoji;

    @Column(nullable = false)
    private String videoUrl;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String resolution;
}
