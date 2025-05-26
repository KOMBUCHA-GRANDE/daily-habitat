package com.kombuchagrande.dailyhabit.entity;

import com.kombuchagrande.dailyhabit.entity.base.BaseSoftDeletableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "graduations")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Graduation extends BaseSoftDeletableEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Column(nullable = false)
    private double completionRate;

    @Column(nullable = false)
    private int maxStackCount;

    @Column(nullable = false)
    private int totalParticipationDays;

    @Column(nullable = false)
    private double averageMoodScore;

    @Column(nullable = false)
    private double averageDifficulty;

    @Column(columnDefinition = "TEXT")
    private String selfReflection;

    @Column(columnDefinition = "TEXT")
    private String finalComment;

    @Column(nullable = false)
    private String photoUrl;

    @Column(nullable = false)
    private String videoUrl;

}
