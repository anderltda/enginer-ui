package br.com.enginer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication
public class EnginerApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(EnginerApplication.class);
		// habilita coleta dos "startup steps" que o /actuator/startup precisa
		app.setApplicationStartup(new BufferingApplicationStartup(2048));
		app.run(args);
	}

}
