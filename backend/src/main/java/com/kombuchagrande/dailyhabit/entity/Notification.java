package com.kombuchagrande.dailyhabit.entity;

import com.kombuchagrande.dailyhabit.converter.JsonToMapConverter;
import com.kombuchagrande.dailyhabit.entity.base.BaseSoftDeletableEntity;
import com.kombuchagrande.dailyhabit.entity.enums.NotificationType;
import com.kombuchagrande.dailyhabit.entity.enums.TargetType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "notifications")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseSoftDeletableEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(nullable = false)
    private Long targetId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    @Convert(converter = JsonToMapConverter.class)
    @Column(columnDefinition = "JSON")
    private Map<String, Objects> meta; //Json

    @Column(length = 100)
    private String title;

    private String content;

    @Column(nullable = false)
    private Boolean isRead;
}
