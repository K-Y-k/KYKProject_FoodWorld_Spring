package kyk.SpringFoodWorldProject.comment.domain.entity;

import kyk.SpringFoodWorldProject.board.domain.entity.BaseTimeEntity;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "COMMENTS")
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String writer;

    @Column
    private String content;

    @Column(insertable = false, updatable = false, columnDefinition = "integer default 0", nullable = false)
    private int likeCount;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="board_id")
    private Board board;

    public Comment(String writer, String content, Member member, Board board) {
        this.writer = writer;
        this.content = content;
        this.member = member;
        this.board = board;
    }


    // 변경 감지 메서드
    public void updateComment(String content){
        this.content = content;
    }


}
