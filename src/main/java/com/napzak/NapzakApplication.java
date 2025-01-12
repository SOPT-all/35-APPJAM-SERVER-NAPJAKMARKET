package com.napzak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NapzakApplication {

	public static void main(String[] args) {
		SpringApplication.run(NapzakApplication.class, args);
	}

}
