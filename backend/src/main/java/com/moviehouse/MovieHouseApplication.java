package com.moviehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MovieHouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieHouseApplication.class, args);
	}

}
