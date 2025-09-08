package com.kombuchagrande.dailyhabit.entity;


import com.kombuchagrande.dailyhabit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token_id", nullable = false)
    private String tokenId;
}
// Todo 나중에 인덱스 추가 (user_Id),
// Todo tokenId 유니크 조건 추가 (인덱스 자동 추가)