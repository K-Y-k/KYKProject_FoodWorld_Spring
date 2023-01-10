package kyk.SpringFoodWorldProject.board.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 전체 글 조회시 전송 객체
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {

    private Long id;

    private String title;

    private LocalDateTime createdDate;

    private Long count;

    private Long likeCount;

    private String titleSearchKeyword;

    private String writerSearchKeyword;

}
