package com.yuseogi.tradeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.yuseogi.tradeservice", "com.yuseogi.common", "com.yuseogi.userservice", "com.yuseogi.storeservice"})
public class TradeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeServiceApplication.class, args);
	}

}
