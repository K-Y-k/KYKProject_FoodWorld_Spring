package kyk.SpringFoodWorldProject.board.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 글 수정시 전송될 전송객체
 */
@Getter @Setter
public class BoardUpdateDto {

    private String title;
    private String content;

    public BoardUpdateDto() {
    }

    public BoardUpdateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
