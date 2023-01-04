package kyk.SpringFoodWorldProject;

import kyk.SpringFoodWorldProject.domain.member.config.SpringDataJpaMemberConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(SpringDataJpaMemberConfig.class)
@SpringBootApplication
public class SpringFoodWorldProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringFoodWorldProjectApplication.class, args);
	}

}
