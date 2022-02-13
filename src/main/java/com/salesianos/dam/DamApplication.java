package com.salesianos.dam;

import com.salesianos.dam.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class DamApplication {

	public static void main(String[] args) {
		SpringApplication.run(DamApplication.class, args);
	}

}
