package kyk.SpringFoodWorldProject.board.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 글 수정시 전송될 전송객체
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class BoardUpdateDto {

    private String title;
    private String content;

}
