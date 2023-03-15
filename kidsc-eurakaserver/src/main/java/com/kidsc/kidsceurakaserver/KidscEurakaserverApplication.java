package com.kidsc.kidsceurakaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class KidscEurakaserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(KidscEurakaserverApplication.class, args);
	}

}
