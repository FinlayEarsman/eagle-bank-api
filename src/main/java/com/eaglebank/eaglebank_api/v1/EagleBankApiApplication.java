package com.eaglebank.eaglebank_api.v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class EagleBankApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EagleBankApiApplication.class, args);
	}

}
