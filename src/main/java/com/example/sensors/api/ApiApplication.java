package com.example.sensors.api;

import com.example.sensors.api.argstore.ArgStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Error: argument mismatch. Please make sure to only pass the configuration file URL");
			System.exit(1);
		}

		ArgStore.url = args[0];

		SpringApplication.run(ApiApplication.class, args);
	}

}
