package com.Abhijith.UiGatewayService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.Abhijith.UiGatewayService.client")
public class UiGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UiGatewayServiceApplication.class, args);
	}

}
