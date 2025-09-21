package com.kombuchagrande.dailyhabit;

import com.kombuchagrande.dailyhabit.oidc.provider.OidcProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(OidcProperties.class)
@SpringBootApplication
public class DailyHabitApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailyHabitApplication.class, args);
	}

}
