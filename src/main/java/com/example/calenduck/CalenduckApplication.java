package com.example.calenduck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@EnableCaching
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@Slf4j
public class CalenduckApplication {

	public static void main(String[] args) {
		loadEnvFile();
		SpringApplication.run(CalenduckApplication.class, args);
	}

	private static void loadEnvFile() {
		Path envPath = Path.of(".env");
		if (!Files.exists(envPath)) {
			return;
		}
		try {
			Files.readAllLines(envPath).stream()
					.filter(line -> !line.isBlank() && !line.startsWith("#"))
					.forEach(line -> {
						int idx = line.indexOf('=');
						if (idx > 0) {
							String key = line.substring(0, idx).trim();
							String value = line.substring(idx + 1).trim();
							if (System.getProperty(key) == null && System.getenv(key) == null) {
								System.setProperty(key, value);
							}
						}
					});
			log.info(".env 파일 로드 완료");
		} catch (IOException e) {
			log.warn(".env 파일 읽기 실패: {}", e.getMessage());
		}
	}

}
