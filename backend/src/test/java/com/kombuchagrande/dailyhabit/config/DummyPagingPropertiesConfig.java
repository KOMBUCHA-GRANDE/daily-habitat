package com.kombuchagrande.dailyhabit.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.util.ReflectionTestUtils;

@TestConfiguration
public class DummyPagingPropertiesConfig {
  @Bean
  public PagingProperties pagingProperties() {
    PagingProperties props = new PagingProperties();
    ReflectionTestUtils.setField(props, "defaultSize", 10);
    return props;
  }
}
