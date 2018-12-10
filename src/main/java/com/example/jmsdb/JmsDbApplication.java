package com.example.jmsdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ComponentScan
@SpringBootApplication
public class JmsDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(JmsDbApplication.class, args);
	}
}
