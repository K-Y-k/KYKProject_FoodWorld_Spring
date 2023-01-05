package kyk.SpringFoodWorldProject.board.repository;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPABoardRepository extends JpaRepository<Board, Long> {
}
