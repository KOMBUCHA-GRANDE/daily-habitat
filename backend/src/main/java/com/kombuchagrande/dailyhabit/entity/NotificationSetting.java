package com.kombuchagrande.dailyhabit.entity;

import com.kombuchagrande.dailyhabit.entity.base.BaseSoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_settings")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationSetting extends BaseSoftDeletableEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long habitId;

    @Column(nullable = false)
    private LocalDateTime recordNotificationTime;

    @Column(nullable = false)
    private boolean recordNotificationEnabled;

    @Column(nullable = false)
    private boolean weeklyReviewNotificationEnabled;

    @Column(nullable = false)
    private boolean monthlyReviewNotificationEnabled;
}


