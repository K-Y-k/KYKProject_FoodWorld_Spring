package kyk.SpringFoodWorldProject.comment.domain.entity;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String boardId;

    @Column(nullable = false)
    private String writer;

    @Column
    private String content;

    @Column
    @CreatedDate
    private LocalDateTime createdDate;

    @Column
    private int likeCount;



}
