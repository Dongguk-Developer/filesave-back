package com.example.filesave_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FilesaveApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilesaveApplication.class, args);
	}

}
