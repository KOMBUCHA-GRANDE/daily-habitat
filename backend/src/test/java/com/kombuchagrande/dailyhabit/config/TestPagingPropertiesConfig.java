package com.kombuchagrande.dailyhabit.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.util.ReflectionTestUtils;

@TestConfiguration
@EnableConfigurationProperties(PagingProperties.class)
public class TestPagingPropertiesConfig {
  @Bean
  public PagingProperties pagingProperties() {
    PagingProperties p = new PagingProperties();
    ReflectionTestUtils.setField(p, "defaultSize", 10);
    return p;
  }
}
