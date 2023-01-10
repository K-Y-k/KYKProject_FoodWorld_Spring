package kyk.SpringFoodWorldProject.board.domain.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
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

    @Column(updatable = false)
    private String fileName;

    @Column(updatable = false)
    private String filePath;
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
