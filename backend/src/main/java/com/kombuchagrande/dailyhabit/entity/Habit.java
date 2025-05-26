package com.kombuchagrande.dailyhabit.entity;

import com.kombuchagrande.dailyhabit.entity.base.BaseSoftDeletableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "habits")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Habit extends BaseSoftDeletableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    private Integer emoji;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false, length = 7)
    private String frequency;

    @Column(nullable = false, length = 10)
    private String backgroundColor;

    @Column(nullable = false)
    private int stackCount;

    @Column(nullable = false)
    private LocalDate stackStartDate;

    @Column(nullable = false)
    private LocalDate nextScheduledDate;

    @Column(nullable = false)
    private boolean isGraduated;

    private LocalDate graduatedAt;

    @Column(nullable = false)
    private int maxStackCount;
}
