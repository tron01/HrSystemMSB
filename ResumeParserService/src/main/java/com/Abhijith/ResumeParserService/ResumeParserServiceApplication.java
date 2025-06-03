package com.Abhijith.ResumeParserService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
public class ResumeParserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumeParserServiceApplication.class, args);
	}

}
