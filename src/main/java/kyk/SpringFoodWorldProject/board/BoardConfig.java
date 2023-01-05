package kyk.SpringFoodWorldProject.board;

import kyk.SpringFoodWorldProject.board.repository.BoardRepository;
import kyk.SpringFoodWorldProject.board.repository.JPABoardRepository;
import kyk.SpringFoodWorldProject.board.repository.SpringDataJpaBoardRepository;
import kyk.SpringFoodWorldProject.board.service.BoardService;
import kyk.SpringFoodWorldProject.board.service.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BoardConfig {

    private final JPABoardRepository repository;

    @Bean
    public BoardService boardService() {
        return new BoardServiceImpl(boardRepository());
    }

    @Bean
    public BoardRepository boardRepository() {
        return new SpringDataJpaBoardRepository(repository);
    }
}
