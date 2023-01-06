package kyk.SpringFoodWorldProject.board.domain.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter @Setter
@Entity
public class Board extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty
    @Column
    private String title;

    @NotEmpty
    @Column
    private String content;

    @Column(updatable = false)
    private String writer;

    @Column(insertable = false, updatable = false, columnDefinition = "number default 0")
    private Long count;

    @Column(insertable = false, updatable = false, columnDefinition = "number default 0")
    private Long likeCount;

//    @Column(insertable = false, updatable = false, columnDefinition = "date default sysdate")
//    private Date createDate;

    public Board() {
    }

    public Board(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
}
