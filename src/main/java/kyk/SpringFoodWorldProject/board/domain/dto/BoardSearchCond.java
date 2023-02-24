package kyk.SpringFoodWorldProject.board.domain.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 검색 조건 객체
 */
@Getter @Setter
public class BoardSearchCond {
    private String writerSearchKeyword;
    private String titleSearchKeyword;

}
