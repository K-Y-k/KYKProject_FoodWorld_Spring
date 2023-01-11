package kyk.SpringFoodWorldProject.comment.domain.dto;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comments;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String content;
    private Member member;
    private Board board;
//    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));


    // Dto -> Entity
    public Comments toEntity() {
        Comments comments = Comments.builder()
                .id(id)
                .content(content)
                .member(member)
                .board(board)
                .build();
        return comments;
    }

}
