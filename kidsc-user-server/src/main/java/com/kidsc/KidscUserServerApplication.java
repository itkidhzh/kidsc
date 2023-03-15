package com.kidsc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.kidsc.user.mapper"})
public class KidscUserServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KidscUserServerApplication.class, args);
	}

}
