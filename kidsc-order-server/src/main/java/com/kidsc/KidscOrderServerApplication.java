package com.kidsc;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.kidsc.order.mapper"})
@EnableFeignClients
@EnableTransactionManagement
@EnableAutoDataSourceProxy
public class KidscOrderServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KidscOrderServerApplication.class, args);
	}

}
