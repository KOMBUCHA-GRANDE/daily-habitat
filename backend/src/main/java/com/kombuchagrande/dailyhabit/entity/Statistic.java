package com.kombuchagrande.dailyhabit.entity;

import com.kombuchagrande.dailyhabit.entity.base.BaseSoftDeletableEntity;
import com.kombuchagrande.dailyhabit.entity.enums.StatKeyType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "statistics")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Statistic extends BaseSoftDeletableEntity {

    @Column(nullable = false)
    private Long reviewId;

    @Enumerated(EnumType.STRING)
    private StatKeyType statKeyType;

    @Column(nullable = false)
    private double statValue;
}
