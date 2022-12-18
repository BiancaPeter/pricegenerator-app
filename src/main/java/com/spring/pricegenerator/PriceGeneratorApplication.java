package com.spring.pricegenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PriceGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceGeneratorApplication.class, args);
	}

}
