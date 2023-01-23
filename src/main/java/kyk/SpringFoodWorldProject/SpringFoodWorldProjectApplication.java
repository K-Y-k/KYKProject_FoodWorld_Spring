package kyk.SpringFoodWorldProject;


import kyk.SpringFoodWorldProject.board.BoardConfig;
import kyk.SpringFoodWorldProject.member.MemberConfig;
import kyk.SpringFoodWorldProject.web.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Import({MemberConfig.class, BoardConfig.class, WebConfig.class})
@SpringBootApplication
public class SpringFoodWorldProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringFoodWorldProjectApplication.class, args);
	}
}
