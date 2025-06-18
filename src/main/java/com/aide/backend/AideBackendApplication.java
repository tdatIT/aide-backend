package com.aide.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AideBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AideBackendApplication.class, args);
	}

}
