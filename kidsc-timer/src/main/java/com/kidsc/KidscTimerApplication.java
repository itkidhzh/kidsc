package com.kidsc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@EnableEurekaClient
public class KidscTimerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KidscTimerApplication.class, args);
	}

}
