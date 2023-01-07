package kyk.SpringFoodWorldProject.board.service;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardDto;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateDto;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.repository.BoardRepository;
import kyk.SpringFoodWorldProject.board.repository.SpringDataJpaBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    @Override
    public Board save(Board board) {
        return boardRepository.save(board);
    }

    @Override
    public void update(Long boardId, BoardUpdateDto updateParam) {
        boardRepository.update(boardId, updateParam);
    }

    @Override
    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }

    @Override
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public Page<Board> pageList(Pageable pageable) {
        return boardRepository.pageList(pageable);
    }

    @Override
    public void delete(Long boardId) {
        boardRepository.delete(boardId);
    }
}
