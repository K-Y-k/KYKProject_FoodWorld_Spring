package kyk.SpringFoodWorldProject.board.domain.entity;

import kyk.SpringFoodWorldProject.comment.domain.entity.Comments;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter @Setter
@Entity
@AllArgsConstructor
public class Board extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 60)
    private String title;

    @Column
    private String content;

    @Column(updatable = false)
    private String writer;

    @Column(insertable = false, updatable = false, columnDefinition = "integer default 0", nullable = false)
    private int count;

    @Column(insertable = false, updatable = false, columnDefinition = "integer default 0", nullable = false)
    private int likeCount;

    // @ManyToOne의 기본 Fetch 전략은 EAGER(즉시 로딩)이지만 필요하지 않은 쿼리도 JPA에서 함께 조회하기 때문에 N+1 문제를 야기할 수 있어, Fetch 전략을 LAZY(지연 로딩)로 설정
    @ManyToOne(fetch = FetchType.LAZY) // Board 입장에서는 member와 다대일 관계이므로
    @JoinColumn(name = "member_id") // Board 엔티티는 Member 엔티티의 id 필드를 "user_id"라는 이름으로 외래 키를 가짐
    private Member member;

    // 양방향 관계를 맺어줌
    // 게시글이 삭제되면 댓글 또한 삭제되어야 하기 때문에 CascadeType.REMOVE 속성을 사용
    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)  // @OneToMany의 기본 Fetch 전략은 LAZY(지연 로딩)
    @OrderBy("id asc") // 댓글 정렬
    private List<Comments> comment;



//    @Column(updatable = false)
//    private String fileName;

//    @Column(updatable = false)
//    private String filePath;

//    @Column(updatable = false)
//    private MultipartFile attachFile;

//    @Column(updatable = false)
//    private List<MultipartFile> imageFiles;


    public Board() {
    }

    public Board(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
}
