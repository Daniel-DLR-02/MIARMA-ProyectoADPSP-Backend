package com.salesianos.dam;

import com.salesianos.dam.config.StorageProperties;
import com.salesianos.dam.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class DamApplication {

	public static void main(String[] args) {
		SpringApplication.run(DamApplication.class, args);
	}

	@Bean
	public CommandLineRunner init (StorageService storageService){
		return args -> {
			storageService.deleteAll();
			storageService.init();

		};
	}

}
