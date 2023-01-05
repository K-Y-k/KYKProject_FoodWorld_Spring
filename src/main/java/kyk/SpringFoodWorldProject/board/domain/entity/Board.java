package kyk.SpringFoodWorldProject.board.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter @Setter
@Entity
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty
    @Column
    private String title;

    @NotEmpty
    @Column
    private String content;

    @NotEmpty
    @Column
    private String writer;

    @NotEmpty
    @Column
    private Date createDate;

    @NotEmpty
    @Column
    private int count;

    @NotEmpty
    @Column
    private int likeCount;

    public Board() {
    }

    public Board(String title, String content, String writer, Date createDate, int count, int likeCount) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createDate = createDate;
        this.count = count;
        this.likeCount = likeCount;
    }
}
