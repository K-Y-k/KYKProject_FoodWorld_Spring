package kyk.SpringFoodWorldProject.board.service;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardUploadDto;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateDto;
import kyk.SpringFoodWorldProject.board.domain.entity.UploadFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BoardService {

    /**
     * 글 저장
     */
    Long upload(Long memberId, BoardUploadDto boardDto, MultipartFile file) throws IOException;

    /**
     * 글 수정
     */
    Long updateBoard(Long boardId, BoardUpdateDto boardUpdateDto);

    /**
     * 글 id로 조회
     */
    Optional<Board> findById(Long id);

    /**
     * 글 모두 조회
     */
    List<Board> findAll();

    /**
     * 글 페이징 처리
     */
    Page<Board> pageList(Pageable pageable);

    /**
     * 글 검색
     */
    Page<Board> findByTitleContaining(String titleSearchKeyword, Pageable pageable);
    Page<Board> findByWriterContaining(String writerSearchKeyword, Pageable pageable);
    Page<Board> findByTitleContainingAndWriterContaining(String titleSearchKeyword, String writerSearchKeyword, Pageable pageable);

    /**
     * 글 삭제
     */
    void delete(Long boardId);


    /**
     * 조회수 카운트
     */
    int updateCount(Long boardId);


    /**
     * 좋아요 개수 최신화
     */
    void updateLikeCount(Long boardId, int likeCount);
}
