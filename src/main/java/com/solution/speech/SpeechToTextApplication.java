package com.solution.speech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class SpeechToTextApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpeechToTextApplication.class, args);
	}

}
