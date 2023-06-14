package com.example.calenduck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@Slf4j
public class CalenduckApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalenduckApplication.class, args);
	}

}
