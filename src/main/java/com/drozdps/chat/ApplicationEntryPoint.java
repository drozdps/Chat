package com.drozdps.chat;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
@EntityScan(basePackages = "com.drozdps.chat")
@EnableAutoConfiguration

// This is an entry point for my spring boot application
public class ApplicationEntryPoint {
	public static void main(String[] args) {
		SpringApplication.run(ApplicationEntryPoint.class, args);
	}
}
