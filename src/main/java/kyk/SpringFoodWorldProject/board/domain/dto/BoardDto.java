package kyk.SpringFoodWorldProject.board.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 전체 글 조회시 전송 객체
 */
@Getter
public class BoardDto {

    private Long id;

    private String title;

    private LocalDateTime createdDate;

    private Long count;

    private Long likeCount;

    private String titleSearchKeyword;

    private String writerSearchKeyword;

    public BoardDto() {
    }

    public BoardDto(Long id, String title, LocalDateTime createdDate, Long count, Long likeCount, String titleSearchKeyword, String writerSearchKeyword) {
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
        this.count = count;
        this.likeCount = likeCount;
        this.titleSearchKeyword = titleSearchKeyword;
        this.writerSearchKeyword = writerSearchKeyword;
    }
}
