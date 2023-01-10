package kyk.SpringFoodWorldProject.board.domain.dto;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시글 정보를 반환할 응답(Response) 클래스
 *  Entity 클래스를 생성자 파라미터로 받아 데이터를 Dto로 변환하여 응답한다.
 *  별도의 전달 객체를 활용해 연관관계를 맺은 엔티티간의 무한참조를 방지한다.
 *  (comments 필드의 List 타입을 DTO 클래스로해서 엔티티간 무한 참조를 방지)
 */
@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime createdDate;
    private int count;
    private Long memberId;
    private List<CommentResponseDto> comments;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.writer = board.getWriter();
        this.content = board.getContent();
        this.createdDate = board.getCreatedDate();
        this.count = board.getCount();
        this.memberId = board.getMember().getId();
        this.comments = board.getComment().stream().map(CommentResponseDto::new).collect(Collectors.toList());
        // Stream을 통해 map으로 new CommentResponseDto에 매핑 해준다. collect를 사용해서 List로 변환한다.
    }
}
