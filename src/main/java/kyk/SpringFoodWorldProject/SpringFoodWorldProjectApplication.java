package kyk.SpringFoodWorldProject;


import kyk.SpringFoodWorldProject.board.BoardConfig;
import kyk.SpringFoodWorldProject.board.service.BoardService;
import kyk.SpringFoodWorldProject.member.MemberConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@EnableJpaAuditing
@Import({MemberConfig.class, BoardConfig.class})
@SpringBootApplication
public class SpringFoodWorldProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringFoodWorldProjectApplication.class, args);
	}
}
