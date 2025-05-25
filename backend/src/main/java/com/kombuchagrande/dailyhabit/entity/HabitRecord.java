package com.kombuchagrande.dailyhabit.entity;

import com.kombuchagrande.dailyhabit.entity.base.BaseSoftDeletableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "habit_records")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HabitRecord extends BaseSoftDeletableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int difficulty;

    private Integer emoji;

    private String photoUrl;

    @Column(nullable = false)
    private String comment;

}
