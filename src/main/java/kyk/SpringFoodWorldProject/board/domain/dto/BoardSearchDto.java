package kyk.SpringFoodWorldProject.board.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 검색 조건 객체
 */
@Getter @Setter
public class BoardSearchDto {
    private String writer;

    private String title;

    public BoardSearchDto() {
    }

    public BoardSearchDto(String writer, String title) {
        this.writer = writer;
        this.title = title;
    }
}
