package com.pdam.tcl;

import com.pdam.tcl.config.StorageProperties;
import com.pdam.tcl.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class TclApplication {

	public static void main(String[] args) {
		SpringApplication.run(TclApplication.class, args);
	}


}
