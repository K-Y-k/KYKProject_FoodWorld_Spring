package kyk.SpringFoodWorldProject.board.repository;

import kyk.SpringFoodWorldProject.board.domain.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 파일 관련 스프링 데이터 JPA 리포지토리
 */
public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
}
