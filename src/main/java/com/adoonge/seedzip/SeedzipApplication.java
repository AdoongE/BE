package com.adoonge.seedzip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@SpringBootApplication
@EnableScheduling
//@EnableJpaAuditing
public class SeedzipApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeedzipApplication.class, args);
	}

}
