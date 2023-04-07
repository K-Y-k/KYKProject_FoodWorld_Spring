package kyk.SpringFoodWorldProject.board.repository;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.entity.BoardFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * BoardRepository 인터페이스의 스프링 데이터 JPA 버전의 구현체
 */
@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class SpringDataJpaBoardRepository implements BoardRepository {

    private final JPABoardRepository boardRepository;

    @Override
    public Board save(Board board) {
        return boardRepository.save(board);
    }


    @Override
    public Optional<Board> findById(Long id) {
//        Optional<Board> optionalBoard = boardRepository.findById(id);
//        if (optionalBoard.isPresent()) {
//            Board board = optionalBoard.get();
//            BoardDto boardDto = BoardDto.toBoardDto(board);
//            return Optional.of(boardDto);
//        } else {
//            return null;
//        }
        return boardRepository.findById(id);
    }

    @Override
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    @Override
    public Page<Board> findPageListByBoardType(Pageable pageable, String boardType) {
        return boardRepository.findPageListByBoardType(pageable, boardType);
    }

    @Override
    public Page<Board> findByTitleContainingAndBoardTypeContaining(String titleSearchKeyword, Pageable pageable, String boardType) {
        return boardRepository.findByTitleContainingAndBoardTypeContaining(titleSearchKeyword, pageable, boardType);
    }

    @Override
    public Page<Board> findByWriterContainingAndBoardTypeContaining(String titleSearchKeyword, Pageable pageable, String boardType) {
        return boardRepository.findByWriterContainingAndBoardTypeContaining(titleSearchKeyword, pageable, boardType);
    }

    @Override
    public Page<Board> findByTitleContainingAndWriterContainingAndBoardTypeContaining(String titleSearchKeyword, String writerSearchKeyword, Pageable pageable, String boardType) {
        return boardRepository.findByTitleContainingAndWriterContainingAndBoardTypeContaining(titleSearchKeyword, writerSearchKeyword, pageable, boardType);
    }

    @Override
    public Long findFirstCursorBoardId(String boardType) {
        return boardRepository.findFirstCursorBoardId(boardType);
    }
    @Override
    public Long findFirstCursorBoardIdInMember(Long memberId, String boardType) {
        return boardRepository.findFirstCursorBoardIdInMember(memberId, boardType);
    }

    @Override
    public Slice<Board> searchBySlice(String memberId, Long lastCursorBoardId, Boolean first, BoardSearchCond boardSearchCond, Pageable pageable, String boardType) {
        return boardRepository.searchBySlice(memberId, lastCursorBoardId, first, boardSearchCond, pageable, boardType);
    }

    @Override
    public void delete(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    @Override
    public int updateCount(Long boardId) {
        return boardRepository.updateCount(boardId);
    }

    @Override
    public int boardsTotalCount(Long memberId) {
        return boardRepository.boardsTotalCount(memberId);
    }

    @Override
    public List<Board> popularBoardList(String boardType) {
        return boardRepository.popularBoardList(boardType);
    }

    public void clear() {
        boardRepository.deleteAll();
    }

}
