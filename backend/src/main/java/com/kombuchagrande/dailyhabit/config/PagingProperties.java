package com.kombuchagrande.dailyhabit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.paging")
public record PagingProperties(
    int defaultSize
) {}
