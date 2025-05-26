package com.kombuchagrande.dailyhabit.entity;

import com.kombuchagrande.dailyhabit.converter.JsonToMapConverter;
import com.kombuchagrande.dailyhabit.entity.base.BaseSoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "cashs")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cash extends BaseSoftDeletableEntity {

    @Column(nullable = false)
    private String attrKey;

    @Convert(converter = JsonToMapConverter.class)
    @Column(nullable = false, columnDefinition = "JSON")
    private Map<String, Objects> meta;
}
