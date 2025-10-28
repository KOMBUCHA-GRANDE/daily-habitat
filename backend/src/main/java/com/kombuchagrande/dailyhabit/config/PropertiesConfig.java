package com.kombuchagrande.dailyhabit.config;


import com.kombuchagrande.dailyhabit.config.oidc.OidcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({PagingProperties.class, OidcProperties.class})
public class PropertiesConfig {
}
