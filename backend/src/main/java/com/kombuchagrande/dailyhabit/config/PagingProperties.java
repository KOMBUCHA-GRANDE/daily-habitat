package com.kombuchagrande.dailyhabit.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "app.paging")
public class PagingProperties {
  private int defaultSize;
}
