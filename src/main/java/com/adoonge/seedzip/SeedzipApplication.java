package com.adoonge.seedzip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableJpaAuditing
public class SeedzipApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeedzipApplication.class, args);
	}

}
