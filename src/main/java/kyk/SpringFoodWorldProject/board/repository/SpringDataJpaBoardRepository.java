package kyk.SpringFoodWorldProject.board.repository;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class SpringDataJpaBoardRepository implements BoardRepository {

    private final JPABoardRepository boardRepository;

    @Override
    public Board save(Board board) {
        return boardRepository.save(board);
    }

    @Override
    public void update(Long boardId, BoardUpdateDto updateParam) {
        // board를 찾음
        Board findBoard = boardRepository.findById(boardId).orElseThrow();

        // 변경 값으로 설정
        // 따로 저장하지 않아도 바꾸기만 해도 자동으로 적용된다.
        // 트랜잭션 커밋되는 시점에 이 설정 업데이트 쿼리를 보낸다.
        findBoard.setTitle(updateParam.getTitle());
        findBoard.setContent(updateParam.getContent());
    }

    @Override
    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }

    @Override
    public List<Board> findAll() {
        return boardRepository.findAll();
    }


}
