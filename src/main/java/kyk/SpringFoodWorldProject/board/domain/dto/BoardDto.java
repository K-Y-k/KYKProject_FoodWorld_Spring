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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    private Long count;

    private Long likeCount;

    public BoardDto() {
    }

    public BoardDto(Long id, String title, LocalDateTime createdDate, Long count, Long likeCount) {
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
        this.count = count;
        this.likeCount = likeCount;
    }
}
