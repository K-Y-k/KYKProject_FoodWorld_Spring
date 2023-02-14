package kyk.SpringFoodWorldProject.board.domain.dto;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 글 저장 전송 객체
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class BoardUploadDto {
    private Long id;

    @NotBlank(message = "제목을 입력해주세요")
    @Size(max = 60, message = "최대 50글자입니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    @Size(max = 500, message = "최대 500글자입니다.")
    private String content;

    private String fileName;

    private String filePath;

    @NotEmpty(message = "게시판을 선택해주세요!")
    private String boardType;

    private String subType;


    // 엔티티에 @setter를 사용하지 않기 위해 dto에서 엔티티로 변환해주는 메서드 적용
    public Board toEntity(Member member, String fileName, String filePath) {
        return Board.builder()
                .title(title)
                .content(content)
                .writer(member.getName())
                .member(member)
                .fileName(fileName)
                .filePath(filePath)
                .boardType(boardType)
                .subType(subType)
                .build();
    }


    public BoardUploadDto(String boardType) {
        this.boardType = boardType;
    }
}