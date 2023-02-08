package com.wtf.cauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(CauthApplication.class, args);
	}

}
