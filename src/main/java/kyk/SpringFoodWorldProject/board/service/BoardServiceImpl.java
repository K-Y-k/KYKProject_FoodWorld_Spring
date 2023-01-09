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
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    @Override
    public void upload(Board board, MultipartFile file) throws IOException {
        // 파일경로 지정
        String fullPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";  // System.getProperty("user.dir")는 resource->static->files로 경로를 정했기에 현재 프로젝트의 경로로 담아줌

        // 파일에 이름을 붙일 랜덤으로 식별자 지정
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();

        // 파일경로와 파일이름 지정한 객체 생성
        File saveFile = new File(fullPath, fileName);
        file.transferTo(saveFile);

        // DB에 저장
        board.setFileName(fileName);
        board.setFilePath("/files/" + fileName);

        boardRepository.save(board);
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

    @Override
    public Page<Board> pageList(Pageable pageable) {
        return boardRepository.pageList(pageable);
    }

    @Override
    public Page<Board> findByTitleContaining(String titleSearchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(titleSearchKeyword, pageable);
    }

    @Override
    public Page<Board> findByWriterContaining(String writerSearchKeyword, Pageable pageable) {
        return boardRepository.findByWriterContaining(writerSearchKeyword, pageable);
    }

    @Override
    public Page<Board> findByTitleAndWriter(String titleSearchKeyword, String writerSearchKeyword, Pageable pageable) {
        return boardRepository.findByTitleAndWriter(titleSearchKeyword, writerSearchKeyword, pageable);
    }

    @Override
    public void delete(Long boardId) {
        boardRepository.delete(boardId);
    }
}
