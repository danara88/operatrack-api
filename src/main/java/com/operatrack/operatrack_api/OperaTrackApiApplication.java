package com.operatrack.operatrack_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OperaTrackApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperaTrackApiApplication.class, args);
	}

}
