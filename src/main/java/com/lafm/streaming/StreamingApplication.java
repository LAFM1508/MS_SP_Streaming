package com.lafm.streaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StreamingApplication {

	public static void main(String[] args) {
		
		System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow", "{}");

		SpringApplication.run(StreamingApplication.class, args);
	}
}
