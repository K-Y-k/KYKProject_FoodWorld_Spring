package kyk.SpringFoodWorldProject.board.domain.dto;

import lombok.Data;

/**
 * 글 수정시 전송될 전송객체
 */
@Data
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
