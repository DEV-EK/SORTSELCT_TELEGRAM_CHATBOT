package com.coolegrams.coolegrams;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@EnableBatchProcessing
@EnableScheduling
@SpringBootApplication
public class CoolegramsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoolegramsApplication.class, args);
	}

}
