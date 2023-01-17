package kyk.SpringFoodWorldProject.board.domain.dto;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 글 저장 전송객체
 */
@Getter
public class BoardDto {

    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    private LocalDateTime createdDate;


    public Board toEntity() {
        Board board = Board.builder()
                .title(title)
                .content(content)
                .build();
        return board;
    }
}
