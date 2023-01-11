package kyk.SpringFoodWorldProject.board.domain.dto;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 전체 글 조회시 전송 객체
 */
@Getter @Setter
public class BoardDto {

    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    private Member member;

    private String createdDate;


    public Board toEntity() {
        Board board = Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .member(member)
                .build();
        return board;
    }
}
