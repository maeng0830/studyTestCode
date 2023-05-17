package sample.cafakiosk.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class CafakioskApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafakioskApplication.class, args);
	}

}
