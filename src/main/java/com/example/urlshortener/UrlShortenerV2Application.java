package com.example.urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UrlShortenerV2Application {
	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerV2Application.class, args);
	}
}
