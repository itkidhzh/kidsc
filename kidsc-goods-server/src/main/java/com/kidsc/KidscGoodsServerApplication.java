package com.kidsc;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.checkerframework.common.aliasing.qual.MaybeAliased;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.kidsc.goods.mapper"})
@EnableFeignClients
@EnableAutoDataSourceProxy //自动事务代理
public class KidscGoodsServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KidscGoodsServerApplication.class, args);
	}

}
