package com.kombuchagrande.dailyhabit;

import com.kombuchagrande.dailyhabit.config.PagingProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(PagingProperties.class)
public class DailyHabitApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailyHabitApplication.class, args);
	}

}
